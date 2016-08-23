package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
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

	public PlayerDataScreen (SpilGame game) {
		super(game);
		//content.add(new VisLabel(getClass().getSimpleName()));
		walletContainer = new VisTable(true);
		inventoryContainer = new VisTable(true);
		content.add(walletContainer).expandX().fillX().row();
		content.add(inventoryContainer).expandX().fillX().row();
		content.add().expand().fill();

		spilSdk.setSpilPlayerDataListener(new SpilPlayerDataListener() {
			@Override public void playerDataAvailable () {
				updateUI();
			}

			@Override public void playerDataUpdated (String reason) {
				updateUI();
			}

			@Override public void playerDataError (SpilErrorCode errorCode) {
				Gdx.app.log(TAG, "Player data error: " + errorCode.getMessage() + " (" + errorCode.getId()+")");
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
		final VisDialog dialog = new VisDialog(count > 0 ? "Add items" : "Subtract items");
		Table content = dialog.getContentTable();
		for (JsonValue item : items) {
			int amount = item.getInt("amount", 0);
			int delta = item.getInt("delta", 0);
			final int id = item.getInt("id", 0);
			int type = item.getInt("type", 0);
			String name = item.getString("name", "no-name");
			VisLabel label = new VisLabel(name + "(" + id + ")");

			VisTextButton button = new VisTextButton(count > 0 ? "Add" : "Subtract");
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
		content.add(close).pad(10);
		dialog.show(stage);
	}

	private void showBundles (JsonValue bundles) {
		final VisDialog dialog = new VisDialog("Get bundles");
		Table content = dialog.getContentTable();
		for (JsonValue bundle : bundles) {
			Gdx.app.log(TAG, "Bundle " + bundle.toString());
			final int id = bundle.getInt("id", 0);
			String name = bundle.getString("name", "no-name");
			VisLabel nameLabel = new VisLabel("Bundle '" + name + "' (" + id + ")");

			String cost = "free!";
			JsonValue prices = bundle.get("prices");
			if (prices != null) {
				cost = "";
				// looks like bundles have only one price, but whatever
				for (JsonValue price : prices) {
					cost += "C " + price.getInt("currencyId", 0) + " x " + price.getInt("value", 0) + " ";
				}
			}
			VisLabel costLabel = new VisLabel(cost);
			VisTable row = new VisTable(true);
			row.add(nameLabel).row();
			row.add(costLabel).row();
			JsonValue items = bundle.get("items");
			if (prices != null) {
				String bundledItems = "";
				// looks like bundles have only one price, but whatever
				for (JsonValue item : items) {
					bundledItems += "I " + item.getInt("id", 0) + " x " + item.getInt("amount", 0) + " ";
				}
				row.add(new VisLabel(bundledItems)).row();
			}
			VisTextButton buy = new VisTextButton("Buy");
			buy.addListener(new ClickListener(){
				@Override public void clicked (InputEvent event, float x, float y) {
					spilSdk.consumeBundle(id, "buy");
					dialog.fadeOut();
				}
			});
			row.add(buy).row();
			content.add(row).pad(10).row();
		}
		content.row();
		VisTextButton close = new VisTextButton("Close");
		close.addListener(new ClickListener(){
			@Override public void clicked (InputEvent event, float x, float y) {
				dialog.fadeOut();
			}
		});
		content.add(close).pad(10);
		dialog.show(stage);
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
