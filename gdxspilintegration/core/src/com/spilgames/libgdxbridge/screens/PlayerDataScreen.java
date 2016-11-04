package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.StringBuilder;
import com.kotcrab.vis.ui.widget.*;
import com.spilgames.libgdxbridge.SpilGame;
import com.spilgames.spilgdxsdk.SpilErrorCode;
import com.spilgames.spilgdxsdk.SpilPlayerDataListener;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public class PlayerDataScreen extends BackScreen {
	private final static String TAG = PlayerDataScreen.class.getSimpleName();

	VisTable walletContainer;
	VisTable inventoryContainer;
	VisTable scrolled;
	VisScrollPane scrollPane;
	IntMap<String> currencyNames = new IntMap<String>();
	IntMap<String> itemNames = new IntMap<String>();

	public PlayerDataScreen (SpilGame game) {
		super(game);
		//content.add(new VisLabel(getClass().getSimpleName()));
		walletContainer = new VisTable(true);
		inventoryContainer = new VisTable(true);
		scrollPane = new VisScrollPane(scrolled = new VisTable());
		scrolled.add(walletContainer).expandX().fillX().row();
		scrolled.add(inventoryContainer).expandX().fillX().row();
		content.add(scrollPane).expand().fill();

		spilSdk.setSpilPlayerDataListener(new SpilPlayerDataListener() {
			@Override public void playerDataAvailable () {
				Gdx.app.log(TAG, "playerDataAvailable");
				updateUI();
			}

			@Override public void playerDataUpdated (String reason, JsonValue updatedData) {
				Gdx.app.log(TAG, "playerDataUpdated( " + reason + ", " +updatedData+")");
				updateUI();
			}

			@Override public void playerDataError (SpilErrorCode errorCode) {
				Gdx.app.log(TAG, "playerDataError" + errorCode.getMessage() + " (" + errorCode.getId()+")");
				dialog("playerDataError", errorCode.getMessage());
			}
		});
		spilSdk.requestPlayerData();
		updateUI();
	}

	private void updateUI () {
		JsonValue userProfile = spilSdk.getUserProfile();
		if (userProfile != null) {
//			Gdx.app.log(TAG, "User profile " + userProfile.prettyPrint(JsonWriter.OutputType.minimal, 0));
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
			walletContainer.clear();
			// this is userProfile.get(wallet)
			String logic = wallet.getString("logic", "<missing>");
			walletContainer.add(new VisLabel("Wallet logic : " + logic)).expand().row();
			walletContainer.add(new VisLabel("Currencies:")).row();
			JsonValue currencies = wallet.get("currencies");
			if (currencies != null) {
				for (JsonValue currency : currencies) {
					int id = currency.getInt("id", -1);
					String name = currency.getString("name", "no-name");
					currencyNames.put(id, name);
					int balance = currency.getInt("currentBalance", 0);
					int type = currency.getInt("type", -1);
					int delta = currency.getInt("delta", 0);
					VisTable container = new VisTable(true);
					walletContainer.add(container).pad(5).fillX().expandX().row();

					VisLabel text = new VisLabel(name + "\nid:" + id + ", b: " + balance);
					container.add(text).fill();
					container.add().expandX();
					VisTable buttons = new VisTable(true);
					container.add(buttons).fill();
					final float width = 100;
					buttons.add(currencyButton(id, 10)).prefWidth(width);
					buttons.add(currencyButton(id, 250)).prefWidth(width).row();
					buttons.add(currencyButton(id, -10)).prefWidth(width);
					buttons.add(currencyButton(id, -250)).prefWidth(width);
				}
			}
		}
		JsonValue inventory = spilSdk.getInventory();
		if (inventory != null) {
			inventoryContainer.clear();
			inventoryContainer.add(new VisLabel("Inventory")).colspan(2).pad(10).row();
			// this is userProfile.get(inventory)
			// items with 0 count are removed
//			Gdx.app.log(TAG, "Inventory " + inventory.prettyPrint(JsonWriter.OutputType.minimal, 0));
			VisTextButton addItem = new VisTextButton("+ITEM");
			inventoryContainer.add(addItem);
			addItem.addListener(new ClickListener(){
				@Override public void clicked (InputEvent event, float x, float y) {
					JsonValue gameData = spilSdk.getGameData();
					if (gameData != null) {
						JsonValue items = gameData.get("items");
						if (items != null) {
							showItems(items, 1);
						} else {
							Gdx.app.log(TAG, "GameData has no items!");
						}
					} else {
						Gdx.app.log(TAG, "GameData is null!");
					}
				}
			});
			VisTextButton subItem = new VisTextButton("-ITEM");
			inventoryContainer.add(subItem).row();
			subItem.addListener(new ClickListener(){
				@Override public void clicked (InputEvent event, float x, float y) {
					JsonValue gameData = spilSdk.getGameData();
					if (gameData != null) {
						JsonValue items = gameData.get("items");
						if (items != null) {
							showItems(items, -1);
						} else {
							Gdx.app.log(TAG, "GameData has no items!");
						}
					} else {
						Gdx.app.log(TAG, "GameData is null!");
					}
				}
			});
			VisTextButton buyBundle = new VisTextButton("BUNDLES");
			inventoryContainer.add(buyBundle).colspan(2).row();
			buyBundle.addListener(new ClickListener(){
				@Override public void clicked (InputEvent event, float x, float y) {
					JsonValue gameData = spilSdk.getGameData();
					if (gameData != null) {
						JsonValue bundles = gameData.get("bundles");
						if (bundles != null) {
							showBundles(bundles);
						} else {
							Gdx.app.log(TAG, "GameData has no bundles!");
						}
					} else {
						Gdx.app.log(TAG, "GameData is null!");
					}
				}
			});
			JsonValue items = inventory.get("items");
			if (items != null) {
				for (JsonValue item : items) {
					int amount = item.getInt("amount", 0);
					int delta = item.getInt("delta", 0);
					int id = item.getInt("id", 0);
					int type = item.getInt("type", 0);
					String name = item.getString("name", "no-name");
//					Gdx.app.log(TAG, "Inventory item, id = " + id + ", name = " + name + ", type = " + type + ", amount = " + amount);
					VisTable row = new VisTable(true);
					row.add(new VisLabel(name + " ("+id+")"));
					row.add().expandX().fillX();
					row.add(new VisLabel(Integer.toString(amount)));
					inventoryContainer.add(row).colspan(2).pad(5).row();
				}
			}
		}
	}

	private void showItems (JsonValue items, final int count) {
		final VisDialog dialog = new VisDialog(count > 0 ? "Add item" : "Remove item");
		Table contentTable = dialog.getContentTable();
		Table content = new Table();
		ScrollPane scrollPane = new VisScrollPane(content);
		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setFadeScrollBars(false);

		contentTable.add(scrollPane).expandX().fillX();
		contentTable.row();
		for (JsonValue item : items) {
			int amount = item.getInt("amount", 0);
			int delta = item.getInt("delta", 0);
			final int id = item.getInt("id", 0);
			int type = item.getInt("type", 0);
			String name = item.getString("name", "no-name");
			VisLabel label = new VisLabel(name + "(" + id + ")");

			VisTextButton button = new VisTextButton(count > 0 ? "Add" : "Remove");
			VisTable row = new VisTable(true);
			row.add(label);
			row.add().expandX().fillX();
			row.add(button);
			content.add(row).pad(10).row();

			button.addListener(new ClickListener() {
				@Override public void clicked (InputEvent event, float x, float y) {
					if (count > 0) {
						spilSdk.addItemToInventory(id, count, "add");
					} else {
						spilSdk.subtractItemFromInventory(id, -count, "subtract");
					}
					dialog.fadeOut();
				}
			});
		}
		VisTextButton close = new VisTextButton("Close");
		close.addListener(new ClickListener(){
			@Override public void clicked (InputEvent event, float x, float y) {
				dialog.fadeOut();
			}
		});
		contentTable.add(close).pad(10);
		dialog.show(stage);
		dialog.setSize(stage.getWidth() * .8f, stage.getHeight() * .8f);
		dialog.centerWindow();
	}

	private void showBundles (JsonValue bundles) {
		final VisDialog dialog = new VisDialog("Get bundles");
		Table contentTable = dialog.getContentTable();
		Table bundlesTable = new Table();
		ScrollPane scrollPane = new VisScrollPane(bundlesTable);
		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setFadeScrollBars(false);

		contentTable.add(scrollPane).expandX().fillX();
		contentTable.row();
		for (JsonValue bundle : bundles) {
//			Gdx.app.log(TAG, "Bundle " + bundle.toString());
			final int id = bundle.getInt("id", 0);
			String name = bundle.getString("name", "no-name");
			VisLabel nameLabel = new VisLabel("Bundle '" + name + "' (" + id + ")");

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
			buy.addListener(new ClickListener(){
				@Override public void clicked (InputEvent event, float x, float y) {
					spilSdk.consumeBundle(id, "buy");
					dialog.fadeOut();
				}
			});
			row.add(buy).row();
			bundlesTable.add(row).pad(10).row();
		}
		VisTextButton close = new VisTextButton("Close");
		close.addListener(new ClickListener(){
			@Override public void clicked (InputEvent event, float x, float y) {
				dialog.fadeOut();
			}
		});
		contentTable.add(close).pad(10);
		dialog.show(stage);
		dialog.setSize(stage.getWidth() * .8f, stage.getHeight() * .8f);
		dialog.centerWindow();
	}

	private Actor currencyButton (final int id, final int count) {
		String text = count>0?"+"+Integer.toString(count):Integer.toString(count);
		VisTextButton button = new VisTextButton(text);
		button.getLabel().setAlignment(Align.right);
		button.addListener(new ClickListener(){
			@Override public void clicked (InputEvent event, float x, float y) {
				if (count > 0) {
					spilSdk.addCurrencyToWallet(id, count, "add");
				} else {
					spilSdk.subtractCurrencyFromWallet(id, -count, "sub");
				}
			}
		});
		return button;
	}
}
