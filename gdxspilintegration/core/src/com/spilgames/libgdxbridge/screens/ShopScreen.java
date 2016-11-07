package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.StringBuilder;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;
import com.spilgames.libgdxbridge.SpilGame;
import com.spilgames.spilgdxsdk.SpilErrorCode;
import com.spilgames.spilgdxsdk.SpilEvent;
import com.spilgames.spilgdxsdk.SpilGameDataListener;
import com.spilgames.spilgdxsdk.SpilPlayerDataListener;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public class ShopScreen extends BackScreen {
	private final static String TAG = ShopScreen.class.getSimpleName();
	private VisTable shop;
	private IntMap<JsonValue> bundleData = new IntMap<JsonValue>();
	private IntMap<String> currencyNames = new IntMap<String>();
	private IntMap<String> itemNames = new IntMap<String>();
	public ShopScreen (SpilGame game) {
		super(game);

		VisTextButton refresh = new VisTextButton("Refresh");
		refresh.addListener(new ClickListener(){
			@Override public void clicked (InputEvent event, float x, float y) {
				// this doesnt seem to work, there is no response :/
				spilSdk.requestGameData();
			}
		});
		content.add(refresh).row();
		shop = new VisTable();
		content.add(shop).expand().fill();

		spilSdk.setSpilGameDataListener(new SpilGameDataListener() {
			@Override public void gameDataAvailable () {
				Gdx.app.log(TAG, "gameDataAvailable");
				updateUI();
			}

			@Override public void gameDataError (SpilErrorCode error) {
				Gdx.app.log(TAG, "gameDataError "  + error.getMessage() + " (" + error.getId()+")");
				dialog("gameDataError", error.getMessage());
			}
		});

		spilSdk.setSpilPlayerDataListener(new SpilPlayerDataListener() {
			@Override public void playerDataAvailable () {
				Gdx.app.log(TAG, "playerDataAvailable");
			}

			@Override public void playerDataUpdated (String reason, JsonValue updatedData) {
				Gdx.app.log(TAG, "playerDataUpdated( " + reason + ", " +updatedData+")");
			}

			@Override public void playerDataError (SpilErrorCode errorCode) {
				Gdx.app.log(TAG, "playerDataError" + errorCode.getMessage() + " (" + errorCode.getId()+")");
				dialog("playerDataError", errorCode.getMessage());
			}
		});
		spilSdk.requestGameData();
		// we force the update as the ios backend doesnt delegate game data events properly
		updateUI();
	}

	private JsonValue getTabs() {
		JsonValue data = spilSdk.getGameData();
		if (data != null) {
			JsonValue shopData = data.get("shop");
			if (shopData != null) {
				if (shopData.isObject()) {
					JsonValue tabs = shopData.get("tabs");
					if (tabs != null) {
						return tabs;
					}
				} else if (shopData.isArray()){
					return shopData;
				}
			}
		}
		return null;
	}

//	TabbedPane tabbedPane;
	private void updateUI () {
		JsonValue tabs = getTabs();
		shop.clear();
		if (tabs == null) {
			shop.add(new VisLabel("No tab data"));
			return;
		}
		JsonValue bundles = spilSdk.getGameData().get("bundles");
		if (bundles != null) {
			for (JsonValue bundle : bundles) {
				bundleData.put(bundle.getInt("id", -1), bundle);
			}
		}
		itemNames.clear();
		JsonValue gameData = spilSdk.getGameData();
		if (gameData != null) {
			JsonValue items = gameData.get("items");
			if (items != null) {
				for (JsonValue item : items) {
					int id = item.getInt("id", -1);
					String name = item.getString("name", "no-name");
					itemNames.put(id, name);
				}
			}
		}
		currencyNames.clear();
		JsonValue wallet = spilSdk.getWallet();
		if (wallet != null) {
			JsonValue currencies = wallet.get("currencies");
			if (currencies != null) {
				for (JsonValue currency : currencies) {
					int id = currency.getInt("id", -1);
					String name = currency.getString("name", "no-name");
					currencyNames.put(id, name);
				}
			}
		}
		TabbedPane tabbedPane = new TabbedPane();
		shop.add(tabbedPane.getTable()).expandX().fillX().row();
		final VisTable selectedTabContent = new VisTable();
		shop.add(selectedTabContent).fill().expand();
		tabbedPane.addListener(new TabbedPaneAdapter(){
			@Override public void switchedTab (Tab tab) {
				selectedTabContent.clear();
				selectedTabContent.add(tab.getContentTable()).fill().expand();
			}
		});

		for (JsonValue tabData : tabs) {
			final String name = tabData.getString("name", "<tab>");
			JsonValue entriesData = tabData.get("entries");
			VisTable paneContent = new VisTable();
			VisScrollPane scrollPane = new VisScrollPane(paneContent);
			final VisTable tabContent = new VisTable();
			tabContent.add(scrollPane).fill().expand();

			if (entriesData != null) {
				for (JsonValue entryData : entriesData) {
					VisTable entry = new VisTable();
					String label = entryData.getString("label", null);
					if (label != null) {
						entry.add(new VisLabel(label)).row();
					}
					final int bundleId = entryData.getInt("bundleId", -1);
					JsonValue bundle = bundleData.get(bundleId, null);
					if (bundle != null) {
						String bundleName = bundle.getString("name", "no-name");
						VisLabel nameLabel = new VisLabel("Bundle '" + bundleName + "' (" + bundleId + ")");

						StringBuilder cost = new StringBuilder("Free!");
						JsonValue prices = bundle.get("prices");
						if (prices != null) {
							cost.setLength(0);
							cost.append("Price: ");
							// looks like bundles have only one price, but whatever
							for (JsonValue price : prices) {
								int currencyId = price.getInt("currencyId", 0);
								cost.append(currencyNames.get(currencyId, "Cid " + currencyId));
								cost.append(" x ");
								cost.append(price.getInt("value", 0));
								cost.append("\n");
							}
							if (cost.length() > 1) {
								cost.setLength(cost.length() - 1);
							}
						}
						VisLabel costLabel = new VisLabel(cost);
						VisTable row = new VisTable(true);
						row.add(nameLabel).row();
						row.add(costLabel).row();
						JsonValue items = bundle.get("items");
						if (prices != null) {
							StringBuilder itemList = new StringBuilder();
							// looks like bundles have only one price, but whatever
							for (JsonValue item : items) {
								int itemId = item.getInt("id", 0);
								itemList.append(itemNames.get(itemId, "Iid " + itemId));
								itemList.append(" x ");
								itemList.append(item.getInt("amount", 0));
								itemList.append("\n");
							}
							if (itemList.length() > 1) {
								itemList.setLength(itemList.length() - 1);
							}
							row.add(new VisLabel(itemList)).row();
						}
						VisTextButton buy = new VisTextButton("Buy");
						buy.addListener(new ClickListener() {
							@Override public void clicked (InputEvent event, float x, float y) {
								spilSdk.consumeBundle(bundleId, "shop");
							}
						});
						row.add(buy).row();
						entry.add(row);
					}
					paneContent.add(entry).row();
				}
			}
			tabbedPane.add(new Tab(false, false) {
				@Override public String getTabTitle () {
					return name;
				}

				@Override public Table getContentTable () {
					return tabContent;
				}
			});
		}

		tabbedPane.switchTab(0);
	}
}
