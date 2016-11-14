package com.spilgames.libgdxbridge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.JsonValue;
import com.kotcrab.vis.ui.widget.*;
import com.spilgames.libgdxbridge.SpilGame;

/**
 * Created by EvilEntity on 20/07/2016.
 */
public class PackagesScreen extends BackScreen {
	private final static String TAG = PackagesScreen.class.getSimpleName();

	VisTable packages;
	public PackagesScreen (final SpilGame game) {
		super(game);
		content.defaults().pad(5);

		VisTextButton refresh = new VisTextButton("Refresh packages");
		refresh.addListener(new ClickListener() {
			@Override public void clicked (InputEvent event, float x, float y) {
				updateUI();
			}
		});

		content.add(refresh).row();
		packages = new VisTable(true);
		packages.setFillParent(true);
		VisScrollPane scrollPane = new VisScrollPane(packages = new VisTable());
		scrollPane.setFadeScrollBars(false);
		content.add(scrollPane).expand().fillX().top();

		updateUI();
	}

	private void updateUI () {
		packages.clear();
		JsonValue allPackages = spilSdk.getAllPackages();
		if (allPackages == null) {
			packages.add(new VisLabel("No package data"));
			spilSdk.requestPackages();
			return;
		}
		updateNames();

		for (JsonValue packageData : allPackages) {
			VisTable packageTable = new VisTable();
			String packageId = packageData.getString("packageId", "-1");
			boolean hasPromotion = packageData.getBoolean("hasPromotion", false);
			if (hasPromotion) {
				JsonValue promotionData = spilSdk.getPromotion(packageId);
				if (promotionData != null) {
					packageId = packageData.getString("packageId", "-1");
					packageTable.add(new VisLabel("Package " + packageId)).row();
					String discountLabel = promotionData.getString("discountLabel", "");
					if (discountLabel.length() > 0) {
						packageTable.add(new VisLabel("Discount " + discountLabel)).row();
					}
					JsonValue items = promotionData.get("items");
					packageTable.add(new VisLabel("Items: ")).row();
					if (items != null) {
						for (JsonValue item : items) {
							int itemId = item.getInt("id", -1);
							int itemValue = item.getInt("value", 0);
							String itemType = item.getString("type");
							String name;
							if (itemType.equals("ITEM")) {
								name = itemNames.get(itemId, "<" + itemId+ ">");
							} else {
								name = currencyNames.get(itemId, "<" + itemId+ ">");
							}
							packageTable.add(new VisLabel(name + " x " + itemValue)).row();
						}
					}
				} else {
					packageTable.add(new VisLabel("Package " + packageId + " has promotion, but promotion data not found!")).row();
					Gdx.app.error(TAG, "Promotion data for package not found.\n" + packageData.toString());
					packageId = "-1";
				}
			} else {
				packageTable.add(new VisLabel("Package " + packageId)).row();
				String discountLabel = packageData.getString("discountLabel", "");
				if (discountLabel.length() > 0) {
					packageTable.add(new VisLabel("Discount " + discountLabel)).row();
				}
				JsonValue items = packageData.get("items");
				packageTable.add(new VisLabel("Items: ")).row();
				if (items != null) {
					for (JsonValue item : items) {
						int itemId = item.getInt("id", -1);
						int itemValue = item.getInt("value", 0);
						String itemType = item.getString("type");
						String name;
						if (itemType.equals("ITEM")) {
							name = itemNames.get(itemId, "<" + itemId+ ">");
						} else {
							name = currencyNames.get(itemId, "<" + itemId+ ">");
						}
						packageTable.add(new VisLabel(name + " x " + itemValue)).row();
					}
				}
			}
			if (!packageId.equals("-1")) {
				final String pid = packageId;
				VisTextButton buy = new VisTextButton("BUY");
				buy.addListener(new ClickListener() {
					@Override public void clicked (InputEvent event, float x, float y) {
						spilSdk.buyPackage(pid);
					}
				});
				packageTable.add(buy).padBottom(16);
			}
			packages.add(packageTable).fillX().expandX();
			packages.row();
		}
	}

	private IntMap<String> currencyNames = new IntMap<String>();
	private IntMap<String> itemNames = new IntMap<String>();
	private void updateNames (){
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
	}
}
