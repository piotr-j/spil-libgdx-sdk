(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
SpilSDK = function (bundleId, appVersion, callback, environment) {
    require("./core_modules/Config.js")({
        bundleId: bundleId,
        appVersion: appVersion,
        environment: environment
    });

    /**
     * core modules
     * @type {*|exports|module.exports}
     */
    var Utils = require("./core_modules/Utils");
    var PreloadQueue = require("./core_modules/PreloadQueue");
    var Events = require("./core_modules/Events");

    /**
     * +
     * @type {*|exports|module.exports}
     */
    var GameData = require("./modules/GameData");
    var EventUtil = require("./modules/EventUtil");
    var ConfigModule = require("./modules/Config");
    var Package = require("./modules/Package");
    var PlayerData = require("./modules/PlayerData");
    var Ads = require("./modules/Ads");
    var ScriptLoader = require("./core_modules/ScriptLoader");


    var modules = [EventUtil, GameData, ConfigModule, Package, PlayerData, Ads];

    function init() {
        PreloadQueue([{
                action: "loadscript",
                args: ["https://payments.spilgames.com/static/javascript/spil/payment.client.js"]
            }, {
                action: "loadscript",
                args: ["https://payments.spilgames.com/static/javascript/spil/payment.portal.js"]
            }, {
                action: function(callback) {
                    ScriptLoader("http://cdn.gameplayer.io/api/js/game.js", function(){
                        Ads.SpilSDK.initAds(callback);
                    });
                }
            }, {
                action: Package.SpilSDK.requestPackages
            }, {
                action: ConfigModule.SpilSDK.refreshConfig
            },{
                action: function (callback) {
                    GameData.SpilSDK.requestGameData(function () {
                        PlayerData.SpilSDK.requestPlayerData(callback);
                    });
                }
            }
            ], function () {

            callback(SpilSDK);
        }
        );

        /**
         * global spilsdk mutate
         */
        SpilSDK = {};
        /**
         * load global SpilSDK properties
         * @type {Array}
         */
        var args = [SpilSDK];
        for (var i = 0; i < modules.length; i++) {
            args.push(modules[i].SpilSDK);
        }
        SpilSDK = Object.assign.apply(null, args);
    }
    init();
};

},{"./core_modules/Config.js":2,"./core_modules/Events":4,"./core_modules/PreloadQueue":5,"./core_modules/ScriptLoader":6,"./core_modules/Utils":7,"./modules/Ads":24,"./modules/Config":25,"./modules/EventUtil":26,"./modules/GameData":27,"./modules/Package":28,"./modules/PlayerData":30}],2:[function(require,module,exports){
var config = null,
    Config = function (_config) {
        config = !_config ? config : _config;
        return config;
    };

module.exports = Config;

},{}],3:[function(require,module,exports){

module.exports = {
    "LoadFailed": {id: 1, name: "LoadFailed", message: "Data container is empty!"},
    "ItemNotFound": {id: 2, name: "ItemNotFound", message: "Item does not exist!"},
    "CurrencyNotFound": {id: 3, name: "CurrencyNotFound", message: "Currency does not exist!"},
    "BundleNotFound": {id: 4, name: "BundleNotFound", message: "Bundle does not exist!"},
    "WalletNotFound": {id: 5, name: "WalletNotFound", message: "No wallet data stored!"},
    "InventoryNotFound": {id: 6, name: "InventoryNotFound", message: "No inventory data stored!"},
    "NotEnoughCurrency": {id: 7, name: "NotEnoughCurrency", message: "Not enough balance for currency!"},
    "ItemAmountToLow": {
            id: 8,
            name: "ItemAmountToLow",
            message: "Could not remove item as amount is too low!"
        },
    "CurrencyOperation": {id: 9, name: "CurrencyOperation", message: "Error updating wallet!"},
    "ItemOperation": {id: 10, name: "ItemOperation", message: "Error updating item to player inventory!"},
    "BundleOperation": {
            id: 11,
            name: "BundleOperation",
            message: "Error adding bundle to player inventory!"
        },
    "PublicGameStateOperation": {
            id: 12,
            name: "UserIdMissing",
            message: "Error adding public game state data! An custom user id must" +
                     " be set in order to save public game state data"
        },
    "GameStateServerError": {
            id: 13,
            name: "OtherUsersGameStateError",
            message: "Error when loading provided user id's game states from the server"
        },
    "DailyBonusServerError": {
            id: 14,
            name: "DailyBonusServerError",
            message: "Error processing the reward from daily bonus"
        },
    "DailyBonusLoadError": {
            id: 15,
            name: "DailyBonusLoadError",
            message: "Error loading the daily bonus page"
        },
    "SplashScreenLoadError": {
        id: 16,
        name: "SplashScreenLoadError",
        message: "Error loading the splash screen"
    }
};

},{}],4:[function(require,module,exports){
var _events = {};

module.exports = {
    publish: function (eventName, args) {
        if (!_events.hasOwnProperty(eventName)) {
            _events[eventName] = {
                subscribers: [],
                args: args,
                published: true
            };
        }
        var event = _events[eventName];
        for (var i = 0; i < event.subscribers.length; i++) {
            subscriber = event.subscribers[i];
            subscriber.fn(args);
            if (subscriber.hasOwnProperty("once")) {
                _events[eventName].subscribers.splice(i, 1);
            }
        }
        event.args = args;
    },
    subscribe: function (eventName, fn) {
        if (!_events.hasOwnProperty(eventName)) {
            _events[eventName] = {
                subscribers: [],
                published: false
            };
        }
        var event = _events[eventName];
        event.subscribers.push({fn: fn});
        if (event.published) {
            fn(event.args);
        }
    },
    subscribeOnce: function (eventName, fn) {
        if (!_events.hasOwnProperty(eventName)) {
            _events[eventName] = {
                subscribers: [],
                published: false
            };
        }
        var event = _events[eventName],
            key = event.subscribers.push({fn: fn, once: true});

        if (event.published) {
            fn(event.args);
            _events[eventName].subscribers.splice(key, 1);
        }
    }
};

},{}],5:[function(require,module,exports){
var ScriptLoader = require("./ScriptLoader");

module.exports = function (actions, onFinishCallback) {

    var counter = 0;

    function loadCallback() {
        counter--;

        if (counter === 0) {
            onFinishCallback();
        }
    }

    function preloadData(method, args) {
        counter++;
        args = args || [];
        args.push(loadCallback);
        method.apply(this, args);
    }

    for (var i = 0; i < actions.length; i++) {

        var preloadConfig = actions[i];

        if (preloadConfig.action === "loadscript") {
            preloadData(ScriptLoader, preloadConfig.args);
        }else {
            preloadData(preloadConfig.action, preloadConfig.args);
        }
    }
};

},{"./ScriptLoader":6}],6:[function(require,module,exports){
module.exports = function (url, callback) {
    var head = document.getElementsByTagName("head")[0],
        script = document.createElement("script");
    script.src = url;

    script.onreadystatechange = callback;
    script.onload = callback;

    head.appendChild(script);
};

},{}],7:[function(require,module,exports){
if (typeof Object.assign !== "function") {
    (function () {
        Object.assign = function (target) {
            "use strict";
            // We must check against these specific cases.
            if (target === undefined || target === null) {
                throw new TypeError("Cannot convert undefined or null to object");
            }

            var output = Object(target);
            for (var index = 1; index < arguments.length; index++) {
                var source = arguments[index];
                if (source !== undefined && source !== null) {
                    for (var nextKey in source) {
                        if (source.hasOwnProperty(nextKey)) {
                            output[nextKey] = source[nextKey];
                        }
                    }
                }
            }
            return output;
        };
    })();
}

var config = require("./Config")(),
    meta = document.querySelector("meta[property=\"portal:site:id\"]"),
    siteId = (meta === null) ? 186 : meta.getAttribute("content"),
    uuid = null,
    locale = null,
    timezoneOffset = null,
    timestampOpened = null,
    sessionId = null,
    lastEventSent = null,
    bundleId = config.bundleId,
    appVersion = config.appVersion || "0.0.1",
    environment = config.environment || "prd";

module.exports = {
    getSiteId: function () {
        return siteId;
    },
    getCurrentTimestamp: function (convertToSeconds) {
        if (!Date.now) {
            Date.now = function () {
                return new Date().getTime();
            };
        }
        return convertToSeconds ? Date.now() / 1000 | 0 : Date.now();
    },
    generateUuid: function () {
        return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0, v = c === "x" ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    },
    getUuid: function () {
        if (!uuid) {
            var UUID_KEY = "uuid";
            uuid = this.getFromStorage(UUID_KEY);

            if (!uuid) {
                uuid = this.generateUuid();
                this.storeInStorage(UUID_KEY, uuid);
            }
        }
        return uuid;
    },
    getLocale: function () {
        if (!locale) {
            locale = navigator.languages ? navigator.languages[0] : (navigator.language || navigator.userLanguage);
        }
        return locale;
    },
    getAppVersion: function () {
        return appVersion;
    },
    getApiVersion: function () {
        return "0.1.0";
    },
    getOs: function () {
        return "html5";
    },
    getBundleId: function () {
        return bundleId;
    },
    getTimezoneOffset: function () {
        return (timezoneOffset) ? timezoneOffset : new Date().getTimezoneOffset().toString();
    },
    getTotalTimeOpen: function () {
        if (!timestampOpened) {
            timestampOpened = this.getCurrentTimestamp(true);
        }
        return (this.getCurrentTimestamp(true) - timestampOpened).toString();
    },
    getSessionId: function () {
        var currentTime = this.getCurrentTimestamp(true);
        if (!sessionId) {
            sessionId = this.generateUuid();
            lastEventSent = currentTime;
        }
        if (currentTime - lastEventSent > 900) {
            sessionId = this.generateUuid();
            timestampOpened = currentTime;
        }
        lastEventSent = currentTime;
        return sessionId;
    },
    getUrl: function () {
        if (environment == 'prd') {
            return "https://apptracker.spilgames.com/v1/native-events/event/" + this.getOs() + "/" + this.getBundleId();
        } else {
            return "http://api-stg.sap.dmz.ams1.spil/v1/native-events/event/" + this.getOs() + "/" + this.getBundleId();
        }
    },
    getFromStorage: function (key) {
        if (!localStorage) {
            throw Error("No local storage available!");
        }
        return localStorage.getItem(key);
    },
    storeInStorage: function (key, value) {
        if (!localStorage) {
            throw Error("No local storage available!");
        }
        localStorage.setItem(key, value);
    }
};

},{"./Config":2}],8:[function(require,module,exports){
function Bundle(bundleData) {
    var BundlePrice = require("./BundlePrice");
    var BundleItem = require("./BundleItem");
    this.id = bundleData.id;
    this.name = bundleData.name;
    this.prices = [];
    for (var i = 0; i < bundleData.prices.length; i++) {
        var price = bundleData.prices[i];
        this.prices.push(new BundlePrice(price));
    }
    this.items = [];
    for (i = 0; i < bundleData.items.length; i++) {
        var item = bundleData.items[i];
        this.items.push(new BundleItem(item));
    }
}
Bundle.prototype.getId = function () {
    return this.id;
};
Bundle.prototype.getName = function () {
    return this.name;
};
Bundle.prototype.getPrices = function () {
    return this.prices;
};
Bundle.prototype.getItems = function () {
    return this.items;
};
Bundle.prototype.getObject = function () {
    result = {
        id: this.id,
        name: this.name,
        prices: [],
        items: []
    };
    for (var i = 0; i < this.prices.length; i++) {
        result.prices.push(this.prices[i].getObject());
    }
    for (i = 0; i < this.items.length; i++) {
        result.items.push(this.items[i].getObject());
    }
    return result;
};

module.exports = Bundle;

},{"./BundleItem":9,"./BundlePrice":10}],9:[function(require,module,exports){
var GameData;

function BundleItem(bundleItemData) {
    GameData = require("../../modules/GameData").SpilSDK;
    this.id = bundleItemData.itemId || bundleItemData.id;
    this.amount = bundleItemData.amount;
}
BundleItem.prototype.getId = function () {
    return this.id;
};
BundleItem.prototype.getAmount = function () {
    return this.amount;
};
BundleItem.prototype.getItem = function () {
    return GameData.getGameData().getItem(this.id);
};
BundleItem.prototype.getObject = function () {
    return {
        id: this.id,
        amount: this.amount
    };
};

module.exports = BundleItem;

},{"../../modules/GameData":27}],10:[function(require,module,exports){
var GameData;

function BundlePrice(bundlePriceData) {
    GameData = require("../../modules/GameData").SpilSDK;
    this.currencyId = bundlePriceData.currencyId || bundlePriceData.id;
    this.value = bundlePriceData.value;
}
BundlePrice.prototype.getCurrencyId = function () {
    return this.currencyId;
};
BundlePrice.prototype.getValue = function () {
    return this.value;
};
BundlePrice.prototype.getCurrency = function () {
    return GameData.getGameData().getCurrency(this.currencyId);
};
BundlePrice.prototype.getObject = function () {
    return {
        currencyId: this.currencyId,
        value: this.value
    };
};

module.exports = BundlePrice;

},{"../../modules/GameData":27}],11:[function(require,module,exports){
function Currency(currencyData) {
    this.id = currencyData.id;
    this.name = currencyData.name;
    this.initialValue = currencyData.initialValue;
    this.type = currencyData.type;
}
Currency.prototype.getId = function () {
    return this.id;
};
Currency.prototype.getName = function () {
    return this.name;
};
Currency.prototype.getInitialValue = function () {
    return this.initialValue;
};
Currency.prototype.getType = function () {
    return this.type;
};

module.exports = Currency;

},{}],12:[function(require,module,exports){
var GameData;

function Entry(entryData) {
    GameData = require("../../modules/GameData").SpilSDK;
    this.bundleId = entryData.bundleId;
    this.label = entryData.label;
    this.position = entryData.position;
}
Entry.prototype.getBundleId = function () {
    return this.bundleId;
};
Entry.prototype.getLabel = function () {
    return this.label;
};
Entry.prototype.getPosition = function () {
    return this.position;
};
Entry.prototype.getBundle = function () {
    return GameData.getGameData().getBundle(this.bundleId);
};
Entry.prototype.getPromotion = function () {
    return GameData.getGameData().getPromotion(this.bundleId);
};

module.exports = Entry;

},{"../../modules/GameData":27}],13:[function(require,module,exports){
var Item, Bundle, Currency, Promotion, Shop;
function GameData(gameData) {
    Item = require("./Item");
    Bundle = require("./Bundle");
    Currency = require("./Currency");
    Promotion = require("./Promotion");
    Shop = require("./Shop");

    this.setItems(gameData.items);
    this.setBundles(gameData.bundles);
    this.setCurrencies(gameData.currencies);
    this.setPromotions(gameData.promotions);
    this.setShop(gameData.shop);
}

GameData.prototype.getItems = function () {
    return this.items;
};
GameData.prototype.setItems = function (items) {
    this.items = [];
    this.itemsDict = {};
    if (!items || !items.length) {
        return;
    }
    for (var i = 0; i < items.length; i++) {
        var item = new Item(items[i]);
        this.items.push(item);
        this.itemsDict[item.getId()] = item;
    }
};
GameData.prototype.getItem = function (itemId) {
    return this.itemsDict[itemId] || null;
};
GameData.prototype.getBundles = function () {
    return this.bundles;
};
GameData.prototype.setBundles = function (bundles) {
    this.bundles = [];
    this.bundlesDict = {};
    if (!bundles || !bundles.length) {
        return;
    }
    for (var i = 0; i < bundles.length; i++) {
        var bundle = new Bundle(bundles[i]);
        this.bundles.push(bundle);
        this.bundlesDict[bundle.getId()] = bundle;
    }
};
GameData.prototype.getBundle = function (bundleId) {
    return this.bundlesDict[bundleId] || null;
};
GameData.prototype.getCurrencies = function () {
    return this.currencies;
};
GameData.prototype.setCurrencies = function (currencies) {
    this.currencies = [];
    this.currenciesDict = {};
    if (!currencies || !currencies.length) {
        return;
    }
    for (var i = 0; i < currencies.length; i++) {
        var currency = new Currency(currencies[i]);
        this.currencies.push(currency);
        this.currenciesDict[currency.getId()] = currency;
    }
};
GameData.prototype.getCurrency = function (currencyId) {
    return this.currenciesDict[currencyId] || null;
};
GameData.prototype.getPromotions = function () {
    return this.promotions;
};
GameData.prototype.setPromotions = function (promotions) {
    this.promotions = [];
    this.promotionsDict = {};
    if (!promotions || !promotions.length) {
        return;
    }
    for (var i = 0; i < promotions.length; i++) {
        var promotion = new Promotion(promotions[i]);
        this.promotions.push(promotion);
        this.promotionsDict[promotion.getBundleId()] = promotion;
    }
};
GameData.prototype.getPromotion = function (bundleId) {
    return this.promotionsDict[bundleId] || null;
};
GameData.prototype.getShop = function () {
    return this.shop;
};
GameData.prototype.setShop = function (shop) {
    this.shop = new Shop(shop);
};

module.exports = GameData;

},{"./Bundle":8,"./Currency":11,"./Item":14,"./Promotion":15,"./Shop":16}],14:[function(require,module,exports){
function Item(itemData) {
    this.id = itemData.id;
    this.name = itemData.name;
    this.initialValue = itemData.initialValue;
    this.type = itemData.type;
}
Item.prototype.getId = function () {
    return this.id;
};
Item.prototype.getName = function () {
    return this.name;
};
Item.prototype.getInitialValue = function () {
    return this.initialValue;
};
Item.prototype.getType = function () {
    return this.type;
};
Item.prototype.getObject = function () {
    return {
        id: this.id
    };
};

module.exports = Item;

},{}],15:[function(require,module,exports){
var GameData;

function Promotion(promotionData) {
    var BundlePrice = require("./BundlePrice");
    GameData = require("../../modules/GameData");
    this.bundleId = promotionData.bundleId;
    this.amount = promotionData.amount;
    this.discount = promotionData.discount;
    this.startDate = promotionData.startDate;
    this.endDate = promotionData.endDate;

    this.prices = [];
    for (var i = 0; i < promotionData.prices.length; i++) {
        var price = promotionData.prices[i];
        this.prices.push(new BundlePrice(price));
    }
}
Promotion.prototype.getBundleId = function () {
    return this.bundleId;
};
Promotion.prototype.getAmount = function () {
    return this.amount;
};
Promotion.prototype.getPrices = function () {
    return this.prices;
};
Promotion.prototype.getDiscount = function () {
    return this.discount;
};
Promotion.prototype.getStartDate = function () {
    return this.startDate;
};
Promotion.prototype.getEndDate = function () {
    return this.endDate;
};
Promotion.prototype.getBundle = function () {
    return GameData.SpilSDK.getGameData().getBundle(this.bundleId);
};

module.exports = Promotion;

},{"../../modules/GameData":27,"./BundlePrice":10}],16:[function(require,module,exports){
function Shop(shopData) {
    var Tab = require("./Tab");

    this.tabs = [];
    if (!shopData || !shopData.length) {
        return;
    }
    for (i = 0; i < shopData.length; i++) {
        var tab = new Tab(shopData[i]);
        this.tabs.push(tab);
    }
}
Shop.prototype.getTabs = function () {
    return this.tabs;
};

module.exports = Shop;

},{"./Tab":17}],17:[function(require,module,exports){
function Tab(tabData) {
    var Entry = require("./Entry");
    this.entries = [];
    for (var i = 0; i < tabData.entries.length; i++) {
        var entry = tabData.entries[i];
        this.entries.push(new Entry(entry));
    }
    this.name = tabData.name;
    this.position = tabData.position;
}
Tab.prototype.getEntries = function () {
    return this.entries;
};
Tab.prototype.getName = function () {
    return this.name;
};
Tab.prototype.getPosition = function () {
    return this.position;
};

module.exports = Tab;

},{"./Entry":12}],18:[function(require,module,exports){
function Inventory(inventoryData) {
    var PlayerItem = require("./PlayerItem");
    this.items = [];
    this.itemsDict = {};
    for (var i = 0; i < inventoryData.items.length; i++) {
        var item = new PlayerItem(inventoryData.items[i]);
        this.items.push(item);
        this.itemsDict[item.getId()] = item;
    }
    this.offset = inventoryData.offset;
    this.logic = inventoryData.logic;
}

Inventory.prototype.getItems = function () {
    return this.items;
};
Inventory.prototype.getItem = function (itemId) {
    return this.itemsDict[itemId] || null;
};
Inventory.prototype.getOffset = function () {
    return this.offset;
};
Inventory.prototype.setOffset = function (offset) {
    this.offset = offset;
};
Inventory.prototype.getLogic = function () {
    return this.logic;
};
Inventory.prototype.setLogic = function (logic) {
    this.logic = logic;
};
Inventory.prototype.addItem = function (item) {
    this.items.push(item);
    this.itemsDict[item.getId()] = item;
};
Inventory.prototype.removeItem = function (itemId) {
    var item = this.itemsDict[itemId],
        index = this.items.indexOf(item);
    if (index > -1) {
        this.items.splice(index, 1);
    }
    delete this.itemsDict[itemId];
};


module.exports = Inventory;

},{"./PlayerItem":20}],19:[function(require,module,exports){
var Currency = require("../gameData/Currency");

function PlayerCurrency(playerCurrencyData) {
    var GameData = require("../../modules/GameData").SpilSDK;
    Currency.call(this, playerCurrencyData);
    var currency = GameData.getGameData().getCurrency(this.id);
    this.name = currency.getName();
    this.type = currency.getType();
    this.initialValue = currency.getInitialValue();
    this.currentBalance = playerCurrencyData.currentBalance;
    this.delta = playerCurrencyData.delta;
}
PlayerCurrency.prototype = Object.create(Currency.prototype);
PlayerCurrency.prototype.constructor = PlayerCurrency;

PlayerCurrency.prototype.getCurrentBalance = function () {
    return this.currentBalance;
};
PlayerCurrency.prototype.setCurrentBalance = function (currentBalance) {
    this.currentBalance = currentBalance;
};
PlayerCurrency.prototype.getDelta = function () {
    return this.delta;
};
PlayerCurrency.prototype.setDelta = function (delta) {
    this.delta = delta;
};

module.exports = PlayerCurrency;

},{"../../modules/GameData":27,"../gameData/Currency":11}],20:[function(require,module,exports){
var Item = require("../gameData/Item");

function PlayerItem(playerItemData) {
    var GameData = require("../../modules/GameData").SpilSDK;
    Item.call(this, playerItemData);
    var item = GameData.getGameData().getItem(this.id);
    this.name = item.getName();
    this.type = item.getType();
    this.amount = playerItemData.amount;
    this.delta = playerItemData.delta;
    this.value = playerItemData.value;
}
PlayerItem.prototype = Object.create(Item.prototype);
PlayerItem.prototype.constructor = PlayerItem;

PlayerItem.prototype.getAmount = function () {
    return this.amount;
};
PlayerItem.prototype.setAmount = function (amount) {
    this.amount = amount;
};
PlayerItem.prototype.getDelta = function () {
    return this.delta;
};
PlayerItem.prototype.setDelta = function (delta) {
    this.delta = delta;
};
PlayerItem.prototype.getValue = function () {
    return this.value;
};

module.exports = PlayerItem;

},{"../../modules/GameData":27,"../gameData/Item":14}],21:[function(require,module,exports){
var PlayerCurrency,
    PlayerItem;

function UpdatedData(updatedData) {
    PlayerCurrency = require("./PlayerCurrency");
    PlayerItem = require("./PlayerItem");

    this.currencies = [];
    this.currenciesDict = {};
    if (updatedData && updatedData.hasOwnProperty("currencies")) {
        this.setCurrencies(updatedData.currencies);
    }
    this.items = [];
    this.itemsDict = {};
    if (updatedData && updatedData.hasOwnProperty("items")) {
        this.setItems(updatedData.items);
    }
}

UpdatedData.prototype.getCurrencies = function () {
    return this.currencies;
};
UpdatedData.prototype.getCurrency = function (currencyId) {
    return this.currenciesDict[currencyId] || null;
};
UpdatedData.prototype.setCurrencies = function (currencies) {
    this.currencies = [];
    this.currenciesDict = {};
    if (!currencies || !currencies.length) {
        return;
    }
    for (var i = 0; i < currencies.length; i++) {
        var currency = new PlayerCurrency(currencies[i]);
        this.currencies.push(currency);
        this.currenciesDict[currency.getId()] = currency;
    }
};
UpdatedData.prototype.addCurrency = function (currency) {
    this.currencies.push(currency);
    this.currenciesDict[currency.getId()] = currency;
};
UpdatedData.prototype.getItems = function () {
    return this.items;
};
UpdatedData.prototype.getItem = function (itemId) {
    return this.itemsDict[itemId] || null;
};
UpdatedData.prototype.setItems = function (items) {
    this.items = [];
    this.itemsDict = {};
    if (!items || !items.length) {
        return;
    }
    for (var j = 0; j < items.length; j++) {
        var item = new PlayerItem(items[j]);
        this.items.push(item);
        this.itemsDict[item.getId()] = item;
    }
};
UpdatedData.prototype.addItem = function (item) {
    this.items.push(item);
    this.itemsDict[item.getId()] = item;
};

module.exports = UpdatedData;

},{"./PlayerCurrency":19,"./PlayerItem":20}],22:[function(require,module,exports){
function UserProfile(userProfileData) {
    var Wallet = require("./Wallet");
    var Inventory = require("./Inventory");
    this.wallet = new Wallet(userProfileData.wallet);
    this.inventory = new Inventory(userProfileData.inventory);
}

UserProfile.prototype.getWallet = function () {
    return this.wallet;
};
UserProfile.prototype.getInventory = function () {
    return this.inventory;
};

module.exports = UserProfile;

},{"./Inventory":18,"./Wallet":23}],23:[function(require,module,exports){
var PlayerCurrency;
function Wallet(walletData) {
    PlayerCurrency = require("./PlayerCurrency");

    this.setCurrencies(walletData.currencies);
    this.offset = walletData.offset;
    this.logic = walletData.logic;
}

Wallet.prototype.getCurrencies = function () {
    return this.currencies;
};
Wallet.prototype.setCurrencies = function (currencies) {
    this.currencies = [];
    this.currenciesDict = {};
    for (var i = 0; i < currencies.length; i++) {
        var currency = new PlayerCurrency(currencies[i]);
        this.currencies.push(currency);
        this.currenciesDict[currency.getId()] = currency;
    }
};
Wallet.prototype.getCurrency = function (currencyId) {
    return this.currenciesDict[currencyId] || null;
};
Wallet.prototype.getOffset = function () {
    return this.offset;
};
Wallet.prototype.setOffset = function (offset) {
    this.offset = offset;
};
Wallet.prototype.getLogic = function () {
    return this.logic;
};
Wallet.prototype.setLogic = function (logic) {
    this.logic = logic;
};
Wallet.prototype.addCurrency = function (currency) {
    this.currencies.push(currency);
    this.currenciesDict[currency.getId()] = currency;
};
Wallet.prototype.removeCurrency = function (currencyId) {
    var currency = this.currenciesDict[currencyId],
        index = this.currencies.indexOf(currency);
    if (index > -1) {
        this.currencies.splice(index, 1);
    }
    delete this.currenciesDict[currencyId];
};

module.exports = Wallet;

},{"./PlayerCurrency":19}],24:[function(require,module,exports){
var EventUtil = require("./EventUtil");

var adsCallbacks = {
        "AdAvailable": function (adType) {},
        "AdNotAvailable": function (adType) {},
        "AdStart": function (adType) {},
        "AdFinished": function (network, adType, reason) {},
};

module.exports = {
    "SpilSDK": {
        initAds: function (callback) {
            EventUtil.sendEvent("advertisementInit", {}, function (responseData) {
                if(responseData.data != undefined
                    && responseData.data.providers != undefined
                    && responseData.data.providers.DFP != undefined){

                        var SpilData = {
                            id: responseData.data.providers.DFP.adUnitID
                        };

                        GameAPI.loadAPI (function (apiInstance) {
                            callback();
                        }, SpilData);
                }else{
                    callback();
                };

            });
        },
        setAdCallbacks: function (listeners) {
            for (var listenerName in listeners) {
                adsCallbacks[listenerName] = listeners[listenerName];
            }
        },
        RequestRewardVideo: function(){
            GameAPI.GameBreak.isRewardAvailable().then(function(){
                adsCallbacks.AdAvailable('rewardVideo');
            }, function(){
                adsCallbacks.AdNotAvailable('rewardVideo');
            });
        },
        PlayVideo: function(){
            GameAPI.GameBreak.isRewardAvailable().then(function(){
                GameAPI.GameBreak.reward(function(){
                    adsCallbacks.AdStart('rewardVideo');
                }, function(data){
                    reason = 'dismiss';
                    if(data.completed){
                        reason = 'close';
                    }
                    adsCallbacks.AdFinished('DFP', 'rewardVideo', reason);
                })
            });
        }
    }
};

},{"./EventUtil":26}],25:[function(require,module,exports){
var EventUtil = require("./EventUtil");
var Events = require("../core_modules/Events");

var config = {},
    configDataCallbacks = {
        "configDataUpdated": function () {}
    };

module.exports = {
    "SpilSDK": {
        refreshConfig: function (callback) {
            EventUtil.sendEvent("requestConfig", {}, function (responseData) {
                config = responseData.data;
                configDataCallbacks.configDataUpdated();
                if (callback) {
                    callback(config);
                }
            });
        },
        getConfigAll: function () {
            return config;
        },
        getConfigValue: function (key) {
            return config[key];
        },
        setConfigDataCallbacks: function (listeners) {
            for (var listenerName in listeners) {
                configDataCallbacks[listenerName] = listeners[listenerName];
            }
        }
    }
};

},{"../core_modules/Events":4,"./EventUtil":26}],26:[function(require,module,exports){
var Utils = require("../core_modules/Utils");

function ajax(method, data, success, failure) {
    var http = window.ActiveX ? new ActiveXObject("Microsoft.XMLHTTP") : new XMLHttpRequest();
    http.onreadystatechange = function () {
        if (http.readyState === 4 && http.status >= 200 && http.status < 300) {
            var response = JSON.parse(http.responseText);
            success(response);
        }else if (http.readyState === 4) {
            failure(JSON.parse(http.responseText));
        }
    };

    http.open(method, Utils.getUrl() + "/" + data.name, true);
    http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    http.send(parseJsonToQuery(data));
}
function parseJsonToQuery(obj) {
    var str = [];
    for (var p in obj) {
        str.push(p + "=" + obj[p]);
    }
    return str.join("&");
}
function getEventData() {
    return {
        "uid": Utils.getUuid(),
        "locale": Utils.getLocale(),
        "appVersion": Utils.getAppVersion(),
        "apiVersion": Utils.getApiVersion(),
        "os": Utils.getOs(),
        "osVersion": "0.0.1",
        "deviceModel": "Web",
        "bundleId": Utils.getBundleId(),
        "timezoneOffset": Utils.getTimezoneOffset(),
        "tto": Utils.getTotalTimeOpen(),
        "sessionId": Utils.getSessionId()
    };
}
function createEvent(eventName, customData) {
    return {
        "name": eventName,
        "data": JSON.stringify(getEventData()),
        "customData": JSON.stringify(customData || {}),
        "ts": Utils.getCurrentTimestamp(false),
        "queued": 0
    };
}

function sendEvent(eventName, data, callback) {
    requestData = createEvent(eventName, data);
    if (!callback) {
        callback = function (responseData) {
            console.log("Got response from " + eventName + ": " + JSON.stringify(responseData));
        };
    }
    ajax("POST", requestData, callback, function (responseData) {
        console.log("Ajax request failed: ");
        console.log(responseData);
    });
}

module.exports = {
    sendEvent: sendEvent,
    "SpilSDK": {
        sendEvent: sendEvent,
        sendCustomEvent: function (eventName, data, callback) {
            sendEvent(eventName, data, callback);
        },
        getUuid: function () {
            return Utils.getUuid();
        }
    }
};

},{"../core_modules/Utils":7}],27:[function(require,module,exports){
var EventUtil = require("./EventUtil");
var Events = require("../core_modules/Events");
var GameData = require("../models/gameData/GameData");
var PlayerData = require("./PlayerData").SpilSDK;
var PlayerCurrency = require("../models/playerData/PlayerCurrency");
var ErrorCodes = require("../core_modules/ErrorCodes");
var gameData;

function getGameData() {
    if (gameData) {
        return gameData;
    }
    try {
        gameData = new GameData(defaultGameData);
        return gameData;
    } catch (err) {
        gameDataCallbacks.gameDataError(ErrorCodes.loadFailed);
        return null;
    }
}

function updateGameData(updatedGameData) {
    gameData = updatedGameData;
}

function processGameData(gameData) {
    storedGameData = getGameData();
    storedGameData.setItems(gameData.items);
    storedGameData.setBundles(gameData.bundles);
    storedGameData.setCurrencies(gameData.currencies);
    storedGameData.setPromotions(gameData.promotions);
    storedGameData.setShop(gameData.shop);

    var wallet = PlayerData.getUserProfile().getWallet();
    for (var i = 0; i < storedGameData.getCurrencies().length; i++) {
        var storedCurrency = storedGameData.getCurrencies()[i];
        if (!wallet.getCurrency(storedCurrency.getId())) {
            wallet.addCurrency(new PlayerCurrency({
                "id": storedCurrency.getId(),
                "currentBalance": storedCurrency.getInitialValue(),
                "delta": storedCurrency.getInitialValue()
            }));
        }
    }
    for (i = 0; i < wallet.getCurrencies().length; i++) {
        var playerCurrency = wallet.getCurrencies()[i];
        if (!storedGameData.getCurrency(playerCurrency.getId())) {
            wallet.removeCurrency(playerCurrency.getId());
        }
    }

    updateGameData(storedGameData);

    gameDataCallbacks.gameDataAvailable();
}

var gameDataCallbacks = {
    gameDataError: function (error) {},
    gameDataAvailable: function () {}
};

module.exports = {
    "SpilSDK": {
        requestGameData: function (callback) {
            EventUtil.sendEvent("requestGameData", {}, function (responseData) {
                processGameData(responseData.data);
                if (callback) {
                    callback(gameData);
                }
            });
        },
        getGameData: function () {
            return getGameData();
        },
        setGameDataCallbacks: function (listeners) {
            for (var listenerName in listeners) {
                gameDataCallbacks[listenerName] = listeners[listenerName];
            }
        }
    }
};

var defaultGameData = {
    "bundles": [],
    "items": [],
    "promotions": [],
    "currencies": [],
    "shop": []
};



},{"../core_modules/ErrorCodes":3,"../core_modules/Events":4,"../models/gameData/GameData":13,"../models/playerData/PlayerCurrency":19,"./EventUtil":26,"./PlayerData":30}],28:[function(require,module,exports){
var EventUtil = require("./EventUtil");
var Payments = require("./Payments");
var Events = require("../core_modules/Events");

var packages = {},
    packagesData = [],
    promotions = {};

function storePackagesAndPromotions(responseData) {
    packages = {};
    packagesData = responseData.data.packages;
    for (var i = 0; i < packagesData.length; i++) {
        var package = packagesData[i];
        packages[package.packageId] = package;
    }
    promotions = {};
    var promotionsData = responseData.data.promotions;
    for (i = 0; i < promotionsData.length; i++) {
        var promotion = promotionsData[i];
        promotions[promotion.promotionId] = promotion;
    }
    return packages;
}

module.exports = {
    "SpilSDK": {
        requestPackages: function (callback) {
            EventUtil.sendEvent("requestPackages", {}, function (responseData) {
                data = storePackagesAndPromotions(responseData);
                if (callback) {
                    callback(data);
                }
            });
        },
        getAllPackages: function () {
            return packagesData;
        },
        getPackage: function (packageId) {
            return packages[packageId] || null;
        },
        getPromotion: function (packageId) {
            return promotions[packageId] || null;
        },
        openPaymentsScreen: function (packageId) {
            EventUtil.sendEvent("prepareWebPayments", {}, function (responseData) {
                Payments.openPaymentsScreen(packageId, responseData.data.referenceNumber);
            });
        }
    }
};



},{"../core_modules/Events":4,"./EventUtil":26,"./Payments":29}],29:[function(require,module,exports){
var EventUtil = require("./EventUtil");

var Utils = require("../core_modules/Utils");
var Config = require("../modules/Config");

module.exports = {
    openPaymentsScreen: function (packageId, referenceNumber) {
        var client = new PaymentClient(),
            options = {
                "siteId": Utils.getSiteId(),
                "gameId": Config.SpilSDK.getConfigValue("payment_game_id"),
                "userId": Utils.getUuid(),
                "token": "",
                "params": JSON.stringify({
                    "package_id": packageId,
                    "reference_number": referenceNumber
                }).replace(/"/g, "%22"),
                "dynamic_pricing": 1
            };
        client.showPaymentSelectionScreen(options);
    }
};

},{"../core_modules/Utils":7,"../modules/Config":25,"./EventUtil":26}],30:[function(require,module,exports){
var ErrorCodes = require("../core_modules/ErrorCodes"),
    EventUtil = require("./EventUtil"),
    UserProfile = require("../models/playerData/UserProfile"),
    UpdatedData = require("../models/playerData/UpdatedData"),
    Wallet = require("../models/playerData/Wallet"),
    Inventory = require("../models/playerData/Inventory"),
    PlayerItem = require("../models/playerData/PlayerItem"),
    PreloadQueue = require("../core_modules/PreloadQueue");

var userProfile,
    lastStoredReason = "",
    timeoutObject = false,
    timeoutObjectTaskKey,
    lastUpdatedData,
    playerDataCallbacks = {
        playerDataError: function (error) {},
        playerDataAvailable: function () {},
        playerDataUpdated: function (reason, updatedData) {}
    };

function getUserProfile() {
    if (userProfile) {
        return userProfile;
    }
    userProfile = new UserProfile(defaultPlayerData);
    return userProfile;
}

function processPlayerData(wallet, inventory) {
    var updated = false,
        updatedData = new UpdatedData(),
        userProfile = getUserProfile();
    wallet = new Wallet(wallet);
    inventory = new Inventory(inventory);
    if (!userProfile) {
        return;
    }
    if (wallet) {
        updated = processWallet(userProfile.getWallet(), wallet) && updated;
        updatedData.setCurrencies(wallet.getCurrencies());
    }
    if (inventory) {
        updated = processInventory(userProfile.getInventory(), inventory) && updated;
        updatedData.setItems(inventory.getItems());
    }

    if (updated) {
        playerDataCallbacks.playerDataUpdated(playerDataUpdateReasons.ServerUpdate, updatedData);
    }

    playerDataCallbacks.playerDataAvailable();
}

function processWallet(oldWallet, newWallet) {
    var storedCurrencies = oldWallet.getCurrencies(),
        receivedCurrencies = newWallet.getCurrencies(),
        updated = false;

    for (var i = 0; i < storedCurrencies.length; i++) {
        storedCurrencies[i].setDelta(0);
    }
    if (oldWallet.getOffset() < newWallet.getOffset() &&
            receivedCurrencies.length > 0 &&
            newWallet.getLogic() === "CLIENT") {
        for (i = 0; i < receivedCurrencies.length; i++) {
            var receivedCurrency = receivedCurrencies[i],
                storedCurrency = oldWallet.getCurrency(receivedCurrency.getId()),
                newBalance;
            if (oldWallet.getOffset() === 0 && newWallet.getOffset() !== 0) {
                newBalance = receivedCurrency.getCurrentBalance();
            } else {
                newBalance = storedCurrency.getCurrentBalance() + receivedCurrency.getDelta();
                newBalance = newBalance >= 0 ? newBalance : 0;
            }
            storedCurrency.setCurrentBalance(newBalance);

            updated = true;
        }
    }
    oldWallet.setOffset(newWallet.getOffset());
    oldWallet.setLogic(newWallet.getLogic());

    return updated;
}

function processInventory(oldInventory, newInventory) {
    var storedItems = oldInventory.getItems(),
        receivedItems = newInventory.getItems(),
        updated = false;

    for (i = 0; i < storedItems.length; i++) {
        storedItems[i].setDelta(0);
    }

    if (oldInventory.getOffset() < newInventory.getOffset() &&
            receivedItems.length > 0 &&
            newInventory.getLogic() === "CLIENT") {
        itemsToBeAdded = [];
        for (i = 0; i < receivedItems.length; i++) {
            var receivedItem = receivedItems[i],
                storedItem = oldInventory.getItem(receivedItem.getId());
            if (storedItem == null) {
                if (receivedItem.getAmount() > 0) {
                    oldInventory.addItem(receivedItem);
                    updated = true;
                }
                continue;
            }

            var newBalance = storedItem.getAmount() + receivedItem.getDelta();
            if (newBalance <= 0) {
                oldInventory.removeItem(storedItem.getId());
            } else {
                storedItem.setAmount(newBalance);
            }
            updated = true;
        }
    }

    oldInventory.setOffset(newInventory.getOffset());
    oldInventory.setLogic(newInventory.getLogic());

    return updated;
}

function updateInventoryWithItem(itemId, amount, action, reason) {
    var userProfile = getUserProfile(),
        gameData = require("./GameData").SpilSDK.getGameData();
    amount = parseInt(amount);
    if (!userProfile || !gameData) {
        playerDataCallbacks.playerDataError(ErrorCodes.LoadFailed);
        return;
    }

    var item = gameData.getItem(itemId);
    if (item === null || amount === undefined || action === undefined || reason === undefined) {
        playerDataCallbacks.playerDataError(ErrorCodes.ItemOperation);
        return;
    }
    var playerItem = new PlayerItem({
            id: item.getId(),
            delta: amount,
            amount: amount
        }),
        inventoryItem = userProfile.getInventory ().getItem(itemId),
        updatedData = new UpdatedData();
    if (inventoryItem) {
        var inventoryItemAmount = inventoryItem.getAmount();
        inventoryItemAmount += (action === "add" ? amount : -amount);
        if (inventoryItemAmount < 0) {

            playerDataCallbacks.playerDataError(ErrorCodes.ItemAmountToLow);
            return;
        }
        inventoryItem.setDelta(amount);
        inventoryItem.setAmount(inventoryItemAmount);
        updatedData.addItem(inventoryItem);
    } else {
        if (action === "add") {
            userProfile.getInventory().addItem(playerItem);
            updatedData.addItem(playerItem);
        } else if (action === "substract") {
            playerDataCallbacks.playerDataError(ErrorCodes.ItemAmountToLow);
            return;
        }
    }

    playerDataCallbacks.playerDataUpdated(reason, updatedData);

    sendUpdatePlayerDataEvent(updatedData, reason, "item", item.getObject());
}

function updateInventoryWithBundle(bundleId, reason, fromShop) {
    var userProfile = getUserProfile(),
        gameData = require("./GameData").SpilSDK.getGameData();
    if (!userProfile || !gameData) {
        playerDataCallbacks.playerDataError(ErrorCodes.LoadFailed);
        return;
    }
    var updatedData = new UpdatedData(),
        bundle = gameData.getBundle(bundleId);
    if (bundle === null || reason === undefined) {
        playerDataCallbacks.playerDataError(ErrorCodes.BundleOperation);
        return;
    }

    var prices = bundle.getPrices(),
        promotion = gameData.getPromotion(bundle.getId()),
        usePromotion = promotion && fromShop;
    if (usePromotion) {
        prices = promotion.getPrices();
    }
    //Look at tempCurrency
    for (var i =  0; i < prices.length; i++) {
        var bundlePrice = prices[i],
            playerCurrency = userProfile.getWallet().getCurrency(bundlePrice.getCurrencyId());
        if (!playerCurrency) {
            playerDataCallbacks.playerDataError(ErrorCodes.CurrencyNotFound);
            return;
        }
        var currentBalance = playerCurrency.getCurrentBalance(),
            updatedBalance = currentBalance - bundlePrice.getValue();
        if (updatedBalance < 0) {
            playerDataCallbacks.playerDataError(ErrorCodes.NotEnoughCurrency);
            return;
        }
        var updatedDelta = playerCurrency.getDelta() - bundlePrice.getValue();
        // Look at this. Make sure the data is sent
        // if (updatedDelta == 0) {
        //     updatedDelta = -bundlePrice.getValue();
        // }
        playerCurrency.setDelta(updatedDelta);
        playerCurrency.setCurrentBalance(updatedBalance);

        updatedData.addCurrency(playerCurrency);
    }

    for (i = 0; i < bundle.getItems().length; i++) {
        var bundleItem = bundle.getItems()[i],
                playerItem = new PlayerItem({
                id: bundleItem.getId()
            }),
            inventoryItem = userProfile.getInventory().getItem(bundleItem.getId()),
            bundleItemAmount = bundleItem.getAmount();
        if (usePromotion) {
            bundleItemAmount *= promotion.getAmount();
        }
        if (inventoryItem != null) {
            var inventoryItemAmount = inventoryItem.getAmount();
            inventoryItemAmount = inventoryItemAmount + bundleItemAmount;

            inventoryItem.setDelta(bundleItemAmount);
            inventoryItem.setAmount(inventoryItemAmount);
            updatedData.addItem(inventoryItem);
        } else {
            playerItem.setDelta(bundleItemAmount);
            playerItem.setAmount(bundleItemAmount);
            userProfile.getInventory().addItem(playerItem);
            updatedData.addItem(playerItem);
        }
    }

    sendUpdatePlayerDataEvent(updatedData, reason, "bundle", bundle.getObject());

    playerDataCallbacks.playerDataUpdated(reason, updatedData);
}

function sendUpdatePlayerDataEvent(updatedData, reason, reportingKey, reportingValue, taskKey) {

    if (taskKey !== undefined) {
        TaskQueue.removeTask("PlayerData", taskKey);
        timeoutObject = false;
    }

    var userProfile = getUserProfile(),
        result = {
            wallet: {
                offset: userProfile.getWallet().getOffset()
            },
            inventory: {
                offset: userProfile.getInventory().getOffset()
            }
        };
    if (updatedData.getCurrencies().length > 0) {
        result.wallet.currencies = [];
        for (var i = 0; i < updatedData.getCurrencies().length; i++) {
            var currency = updatedData.getCurrencies()[i];
            result.wallet.currencies.push({
                id: currency.getId(),
                currentBalance: currency.getCurrentBalance(),
                delta: currency.getDelta()
            });
        }
    }
    if (updatedData.getItems().length > 0) {
        result.inventory.items = [];
        for (var j = 0; j < updatedData.getItems().length; j++) {
            var item = updatedData.getItems()[j];
            result.inventory.items.push({
                id: item.getId(),
                amount: item.getAmount(),
                delta: item.getDelta()
            });
        }
    }
    if (reportingKey && reportingValue) {
        result[reportingKey] = reportingValue;
    }

    if (reason) {
        result.reason = reason;
    }

    updatePlayerData(result);

}

var TaskQueue = {
    getQueue: function (holder) {
        return JSON.parse(localStorage.getItem(holder)) || [];
    },
    addTask: function (holder, task) {
        var queue = this.getQueue(holder),
            taskKey = queue.push({args: task.args});

        localStorage.setItem(holder, JSON.stringify(queue));

        return taskKey - 1;
    },
    runTasks: function (holder, callback) {
        var queue = this.getQueue(holder),
            queueActions = [];
        for (var i = 0; i < queue.length; i++) {

            var task = queue[i];

            if (holder === "PlayerData") {
                queueActions.push({
                    action: mutateWalletTask,
                    args: task.args
                });
            }

        }

        PreloadQueue(queueActions, callback);

        this.clearQueue(holder);
    },
    removeTask: function (holder, taskKey) {
        var queue = this.getQueue(holder);
        queue.splice(taskKey, 1);
        localStorage.setItem(holder, JSON.stringify(queue));
    },
    updateTask: function (holder, taskKey, args) {
        var queue = this.getQueue(holder);
        queue[taskKey].args = args;

        localStorage.setItem(holder, JSON.stringify(queue));
    },
    clearQueue: function (holder) {
        localStorage.setItem(holder, JSON.stringify([]));
    }
};

function mutateWalletTask(requestUpdatedData, reason) {

    requestUpdatedData = new UpdatedData({"currencies": requestUpdatedData.currencies});

    var currencies = requestUpdatedData.getCurrencies(),
        currency,
        userCurrency;

    for (var i = 0; i < currencies.length; i++) {
        currency = currencies[i];

        userCurrency = getUserProfile().getWallet().getCurrency(currency.id);

        userCurrency.setDelta(currency.getDelta());
        userCurrency.setCurrentBalance(currency.getCurrentBalance());
    }


    sendUpdatePlayerDataEvent(requestUpdatedData, reason);
}

var requestUpdatedData = new UpdatedData();

function mutateWallet(currencyId, delta, reason) {

    var userProf = getUserProfile();
    if (!userProf) {
        playerDataCallbacks.playerDataError(ErrorCodes.WalletNotFound);
        return;
    }

    var currency = getUserProfile().getWallet().getCurrency(currencyId);

    if (!currency) {
        playerDataCallbacks.playerDataError(ErrorCodes.CurrencyNotFound);
        return;
    }

    if (currencyId < 0 || reason == null) {
        playerDataCallbacks.playerDataError(ErrorCodes.CurrencyOperation);
        return;
    }

    var updatedBalance = parseFloat(currency.getCurrentBalance()) + parseFloat(delta);


    if (updatedBalance < 0) {
        playerDataCallbacks.playerDataError(ErrorCodes.NotEnoughCurrency);
        return;
    }

    var updatedDelta = delta + currency.getDelta();

    if (updatedDelta === 0) {
        updatedDelta = delta;
    }

    currency.setDelta(updatedDelta);
    currency.setCurrentBalance(updatedBalance);

    if (userProf.getWallet().getLogic() === "CLIENT") {

        requestUpdatedData.addCurrency(currency);

        if (lastStoredReason === reason || lastStoredReason === "") {

            playerDataCallbacks.playerDataUpdated(reason, new UpdatedData({"currencies": [currency]}));

            /**
             * if there is no timeout object yet defined
             */
            if (timeoutObject === false) {
                lastStoredReason = reason;
                lastUpdatedData = requestUpdatedData;
                /**
                 * add task
                 */
                timeoutObjectTaskKey = TaskQueue.addTask("PlayerData", {
                    args: [requestUpdatedData, reason]
                });

                timeoutObject = setTimeout(sendUpdatePlayerDataEvent.bind(
                    null,
                    requestUpdatedData,
                    reason,
                    null, null,
                    timeoutObjectTaskKey
                ), 5000);

            /**
             * if there is allready a timeout object defined,
             * replace the current timeout object with the new updated data and reset the timer
             *
             * update the task to execute with
             */
            }else {
                /** unregister current timeout object
                    and register a new one
                 **/
                clearTimeout(timeoutObject);
                lastUpdatedData = requestUpdatedData;

                TaskQueue.updateTask("PlayerData", timeoutObjectTaskKey, [requestUpdatedData, reason]);

                timeoutObject = setTimeout(sendUpdatePlayerDataEvent.bind(
                    null,
                    requestUpdatedData,
                    reason,
                    null, null,
                    timeoutObjectTaskKey
                ), 5000);
            }

        }else {
            var lastReason = lastStoredReason;

            clearTimeout(timeoutObject);
            timeoutObject = false;
            TaskQueue.removeTask("PlayerData", timeoutObjectTaskKey);
            sendUpdatePlayerDataEvent(requestUpdatedData, lastReason);

            var newUpdatedData = new UpdatedData({"currencies": [currency]});

            sendUpdatePlayerDataEvent(newUpdatedData, reason);
            lastStoredReason = reason;
            lastUpdatedData = newUpdatedData;

            playerDataCallbacks.playerDataUpdated(reason, newUpdatedData);

        }

    }
}

function updatePlayerData(data, callback) {
    EventUtil.sendEvent("updatePlayerData", data, function (responseData) {
        processPlayerData(responseData.data.wallet, responseData.data.inventory);

        if (callback) {
            callback(userProfile);
        }
    });
}

var playerDataUpdateReasons = {
    RewardAds: "Reward Ads",
    ItemBought: "Item Bought",
    ItemSold: "Item Sold",
    EventReward: "Event Reward",
    LoginReward: "Login Reward",
    IAP: "IAP",
    PlayerLevelUp: "Player Level Up",
    LevelComplete: "Level Complete",
    ItemUpgrade: "Item Upgrade",
    BonusFeatures: "Bonus Features",
    Trade: "Trade",
    ClientServerMismatch: "Client-Server Mismatch",
    ItemPickedUp: "Item Picked Up",
    ServerUpdate: "Server Update",
    DailyBonus: "Daily Bonus From Client"
};


module.exports = {
    "SpilSDK": {
        requestPlayerData: function (callback) {

            function request(callback) {
                var userProfile = getUserProfile();
                eventData = {
                    "wallet": {"offset": userProfile.getWallet().getOffset()},
                    "inventory": {"offset": userProfile.getInventory().getOffset()}
                };
                EventUtil.sendEvent("requestPlayerData", eventData, function (responseData) {
                    processPlayerData(responseData.data.wallet, responseData.data.inventory);
                    if (callback) {
                        callback(userProfile);
                    }
                });
            }

            /**
             * if we have task run the tasks
             */
            if (TaskQueue.getQueue("PlayerData").length > 0) {
                request(function () {
                    TaskQueue.runTasks("PlayerData", function () {
                        //request();
                    });
                });

            }else {
                request(callback);
            }
        },
        getWallet: function () {
            var userProf = getUserProfile();
            if (userProf) {
                return userProf.getWallet();
            } else {
                playerDataCallbacks.playerDataError(ErrorCodes.WalletNotFound);
            }
        },
        getInventory: function () {
            return getUserProfile().getInventory();
        },
        getUserProfile: function () {
            return getUserProfile();
        },
        addItemToInventory: function (itemId, amount, reason) {

            if (parseInt(amount) < 0) {
                playerDataCallbacks.playerDataError(ErrorCodes.ItemOperation);
                return;
            }
            updateInventoryWithItem(itemId, amount, "add", reason);
        },
        subtractItemFromInventory: function (itemId, amount, reason) {

            if (parseInt(amount) < 0) {
                playerDataCallbacks.playerDataError(ErrorCodes.ItemOperation);
                return;
            }
            updateInventoryWithItem(itemId, amount, "substract", reason);
        },
        consumeBundle: function (bundleId, reason, fromShop) {
            updateInventoryWithBundle(bundleId, reason, fromShop);
        },
        addCurrencyToWallet: function (currencyId, delta, reason) {

            if (parseInt(delta) < 0) {
                playerDataCallbacks.playerDataError(ErrorCodes.CurrencyOperation);
                return;
            }

            mutateWallet(currencyId, parseFloat(delta), reason);
        },
        subtractCurrencyFromWallet: function (currencyId, delta, reason) {

            if (parseInt(delta) < 0) {
                playerDataCallbacks.playerDataError(ErrorCodes.CurrencyOperation);
                return;
            }

            mutateWallet(currencyId, -Math.abs(delta), reason);
        },
        setPlayerDataCallbacks: function (listeners) {
            for (var listenerName in listeners) {
                playerDataCallbacks[listenerName] = listeners[listenerName];
            }
        }
    }
};

var defaultPlayerData = {
    "inventory": {
        "offset": 0,
        "items": [],
        "logic": "CLIENT"
    },
    "wallet": {
        "offset": 0,
        "logic": "CLIENT",
        "currencies": []
    }
};

},{"../core_modules/ErrorCodes":3,"../core_modules/PreloadQueue":5,"../models/playerData/Inventory":18,"../models/playerData/PlayerItem":20,"../models/playerData/UpdatedData":21,"../models/playerData/UserProfile":22,"../models/playerData/Wallet":23,"./EventUtil":26,"./GameData":27}]},{},[1]);
