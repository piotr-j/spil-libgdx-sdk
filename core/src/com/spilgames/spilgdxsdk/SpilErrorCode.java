package com.spilgames.spilgdxsdk;

/**
 * Created by PiotrJ on 29/07/16.
 */
public enum SpilErrorCode {
	Invalid(-1, "Unknown", "Unknown error id"),
	LoadFailed(1, "LoadFailed", "Data container is empty!"),
	ItemNotFound(2, "ItemNotFound", "Item does not exist!"),
	CurrencyNotFound(3, "CurrencyNotFound", "Currency does not exist!"),
	BundleNotFound(4, "BundleNotFound", "Bundle does not exist!"),
	WalletNotFound(5, "WalletNotFound", "No wallet data stored!"),
	InventoryNotFound(6, "InventoryNotFound", "No inventory data stored!"),
	NotEnoughCurrency(7, "NotEnoughCurrency", "Not enough balance for currency!"),
	ItemAmountToLow(8, "ItemAmountToLow", "Could not remove item as amount is too low!"),
	CurrencyOperation(9, "CurrencyOperation", "Error updating wallet!"),
	ItemOperation(10, "ItemOperation", "Error updating item to player inventory!"),
	BundleOperation(11, "BundleOperation", "Error adding bundle to player inventory!")
	;

	private int id;
	private String name;
	private String message;

	SpilErrorCode(int id, String name, String message) {
		this.id = id;
		this.name = name;
		this.message = message;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getMessage() {
		return this.message;
	}

	private static SpilErrorCode[] all = values();

	public static SpilErrorCode fromId(int id) {
		if (id < 1 || id > 11) return Invalid;
		return all[id];
	}
}
