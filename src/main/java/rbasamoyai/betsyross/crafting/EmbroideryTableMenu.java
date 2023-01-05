package rbasamoyai.betsyross.crafting;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.betsyross.BetsyRoss;
import rbasamoyai.betsyross.config.BetsyRossConfig;
import rbasamoyai.betsyross.data.BetsyRossTags;
import rbasamoyai.betsyross.network.BetsyRossNetwork;
import rbasamoyai.betsyross.network.ServerboundSyncTableDataPacket;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class EmbroideryTableMenu extends AbstractContainerMenu {

	private static final int SPOOL_SLOT_X = 29;
	private static final int SPOOL_SLOT_Y = 33;
	private static final int STICKS_SLOT_X = 63;
	private static final int STICKS_SLOT_Y = 33;
	private static final int OUTPUT_SLOTS_X = 125;
	private static final int OUTPUT_SLOTS_Y = 24;
	private static final int INVENTORY_X = 8;
	private static final int INVENTORY_Y = 106;
	private static final int HOTBAR_X = 8;
	private static final int HOTBAR_Y = 164;
	private static final int INVENTORY_COLUMNS = 9;
	private static final int INVENTORY_ROWS = 3;
	private static final int HOTBAR_COLUMNS = INVENTORY_COLUMNS;

	public static final int SPOOL_INDEX = 0;
	public static final int STICKS_INDEX = SPOOL_INDEX + 1;
	public static final int FLAG_BLOCK_INDEX = STICKS_INDEX + 1;
	public static final int ARMOR_BANNER_INDEX = FLAG_BLOCK_INDEX + 1;
	public static final int FLAG_STANDARD_INDEX = ARMOR_BANNER_INDEX + 1;
	public static final int BANNER_STANDARD_INDEX = FLAG_STANDARD_INDEX + 1;
	private static final int INVENTORY_INDEX_START = BANNER_STANDARD_INDEX + 1;
	private static final int HOTBAR_INDEX_START = INVENTORY_INDEX_START + INVENTORY_ROWS * INVENTORY_COLUMNS;
	private static final int HOTBAR_INDEX_END = HOTBAR_INDEX_START + HOTBAR_COLUMNS;

	private final ContainerLevelAccess access;
	private final IItemHandler container;

	private String url = "";
	private byte width = 1;
	private byte height = 1;

	private final Set<OutputSlot> outputSlots = new HashSet<>();

	public EmbroideryTableMenu(int windowId, Inventory playerInv) {
		this(windowId, playerInv, ContainerLevelAccess.NULL);
	}

	public EmbroideryTableMenu(int windowId, Inventory playerInv, ContainerLevelAccess access) {
		super(BetsyRoss.EMBROIDERY_TABLE_MENU.get(), windowId);
		this.access = access;
		this.container = new ItemStackHandler(6);

		this.addSlot(new SlotItemHandler(this.container, SPOOL_INDEX, SPOOL_SLOT_X, SPOOL_SLOT_Y) {
			@Override public boolean mayPlace(@NotNull ItemStack stack) { return stack.is(BetsyRossTags.FLAG_MATERIAL); }

			@Override
			public void setChanged() {
				super.setChanged();
				EmbroideryTableMenu.this.fillResultSlots(this.getItem());
			}
		});
		this.addSlot(new SlotItemHandler(this.container, STICKS_INDEX, STICKS_SLOT_X, STICKS_SLOT_Y) {
			@Override public boolean mayPlace(@NotNull ItemStack stack) { return stack.is(BetsyRossTags.FLAG_STICK_MATERIAL); }
		});

		this.addOutputSlot(FLAG_BLOCK_INDEX, OUTPUT_SLOTS_X, OUTPUT_SLOTS_Y, TakenItem.FLAG_BLOCK);
		this.addOutputSlot(ARMOR_BANNER_INDEX, OUTPUT_SLOTS_X + 18, OUTPUT_SLOTS_Y, TakenItem.ARMOR_BANNER);
		this.addOutputSlot(FLAG_STANDARD_INDEX, OUTPUT_SLOTS_X, OUTPUT_SLOTS_Y + 18, TakenItem.FLAG_STANDARD);
		this.addOutputSlot(BANNER_STANDARD_INDEX, OUTPUT_SLOTS_X + 18, OUTPUT_SLOTS_Y + 18, TakenItem.BANNER_STANDARD);

		for (int i = 0; i < INVENTORY_ROWS; ++i) {
			for (int j = 0; j < INVENTORY_COLUMNS; ++j) {
				int x = INVENTORY_X + j * 18;
				int y = INVENTORY_Y + i * 18;
				int in = i * INVENTORY_COLUMNS + j + 9;
				this.addSlot(new Slot(playerInv, in, x, y));
			}
		}

		for (int i = 0; i < HOTBAR_COLUMNS; ++i) {
			int x = HOTBAR_X + i * 18;
			this.addSlot(new Slot(playerInv, i, x, HOTBAR_Y));
		}
	}

	private void addOutputSlot(int index, int x, int y, TakenItem mode) {
		OutputSlot slot = new OutputSlot(this.container, index, x, y, mode);
		this.addSlot(slot);
		this.outputSlots.add(slot);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack result = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			result = slotStack.copy();
			if (FLAG_BLOCK_INDEX <= index && index <= BANNER_STANDARD_INDEX) {
				if (!this.moveItemStackTo(slotStack, INVENTORY_INDEX_START, HOTBAR_INDEX_END, true)) return ItemStack.EMPTY;
				slot.onQuickCraft(slotStack, result);
			} else if (index != SPOOL_INDEX && index != STICKS_INDEX) {
				if (slotStack.is(BetsyRossTags.FLAG_MATERIAL)) {
					if (!this.moveItemStackTo(slotStack, SPOOL_INDEX, SPOOL_INDEX + 1, false)) return ItemStack.EMPTY;
				} else if (slotStack.is(BetsyRossTags.FLAG_STICK_MATERIAL)) {
					if (!this.moveItemStackTo(slotStack, STICKS_INDEX, STICKS_INDEX + 1, false)) return ItemStack.EMPTY;
				} else if (index >= INVENTORY_INDEX_START && index < HOTBAR_INDEX_START) {
					if (!this.moveItemStackTo(slotStack, HOTBAR_INDEX_START, HOTBAR_INDEX_END, false)) return ItemStack.EMPTY;
				} else if (index >= HOTBAR_INDEX_START && index < HOTBAR_INDEX_END) {
					if (!this.moveItemStackTo(slotStack, INVENTORY_INDEX_START, HOTBAR_INDEX_START, false)) return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(slotStack, INVENTORY_INDEX_START, HOTBAR_INDEX_END, false)) {
				return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (slotStack.getCount() == result.getCount()) return ItemStack.EMPTY;
			slot.onTake(player, slotStack);
		}
		return result;
	}

	@Override public boolean stillValid(Player player) { return stillValid(this.access, player, BetsyRoss.EMBROIDERY_TABLE_BLOCK.get()); }

	public boolean canSync() {
		for (OutputSlot slot : this.outputSlots) {
			if (!slot.getItem().isEmpty()) return true;
		}
		return false;
	}

	public int getRequiredWool() {
		int areaPerWool = BetsyRossConfig.SERVER.areaPerWool.get();
		if (areaPerWool <= 0) areaPerWool = 1;
		return Mth.ceil((float) this.width * (float) this.height / (float) areaPerWool);
	}

	private boolean hasRequiredWool(ItemStack wool) {
		return wool.getCount() >= this.getRequiredWool();
	}

	protected void fillResultSlots(ItemStack wool) {
		boolean filled = this.hasRequiredWool(wool);
		for (OutputSlot slot : this.outputSlots) {
			slot.propagate(filled);
		}
	}

	protected void onTakeResult(Player player, ItemStack stack, TakenItem mode) {
		for (OutputSlot slot : this.outputSlots) {
			if (slot.mode != mode) slot.set(ItemStack.EMPTY);
		}
		Slot woolSlot = this.slots.get(SPOOL_INDEX);
		if (woolSlot != null) {
			ItemStack item = woolSlot.getItem().copy();
			item.setCount(Math.max(0, item.getCount() - this.getRequiredWool()));
			woolSlot.set(item);
		}
		Slot sticksSlot = this.slots.get(STICKS_INDEX);
		if (sticksSlot != null) {
			ItemStack item = sticksSlot.getItem().copy();
			item.setCount(Math.max(0, item.getCount() - mode.getRequiredSticks()));
			sticksSlot.set(item);
		}
	}

	protected boolean mayPickup(Player player, TakenItem mode) {
		if (this.width <= 0 || this.height <= 0) return false;
		if (this.width > mode.getMaxWidth() && mode.getMaxWidth() > 0) return false;
		if (this.height > mode.getMaxHeight() && mode.getMaxHeight() > 0) return false;
		if (!this.hasRequiredWool(this.container.getStackInSlot(SPOOL_INDEX))) return false;

		if (mode.getRequiredSticks() == 0) return true;
		ItemStack sticks = this.container.getStackInSlot(STICKS_INDEX);
		return sticks.is(BetsyRossTags.FLAG_STICK_MATERIAL) && sticks.getCount() >= mode.getRequiredSticks();
	}

	public void setDataOnServer(String url, byte width, byte height) {
		this.url = url;
		this.width = width;
		this.height = height;
		this.fillResultSlots(this.container.getStackInSlot(SPOOL_INDEX));
	}

	public void setAndSendDataToServer(String url, byte width, byte height) {
		this.url = url;
		this.width = width;
		this.height = height;
		BetsyRossNetwork.NETWORK.sendToServer(new ServerboundSyncTableDataPacket(this.url, this.width, this.height));
	}

	@Override
	public void removed(Player pPlayer) {
		super.removed(pPlayer);
		this.access.execute((level, pos) -> {
			this.clearSlot(pPlayer, this.slots.get(SPOOL_INDEX));
			this.clearSlot(pPlayer, this.slots.get(STICKS_INDEX));
		});
	}

	protected void clearSlot(Player player, Slot slot) {
		if (!player.isAlive() || player instanceof ServerPlayer splayer && splayer.hasDisconnected()) {
			player.drop(slot.getItem().copy(), false);
		} else if (player instanceof ServerPlayer splayer) {
			splayer.getInventory().placeItemBackInInventory(slot.getItem().copy());
		}
	}

	public enum TakenItem {
		FLAG_BLOCK(() -> BetsyRossConfig.SERVER.flagBlockRequiredSticks.get(),
				() -> BetsyRossConfig.SERVER.flagBlockMaxWidth.get(),
				() -> BetsyRossConfig.SERVER.flagBlockMaxHeight.get(),
				() -> BetsyRoss.FLAG_ITEM.get().getDefaultInstance()),
		ARMOR_BANNER(() -> BetsyRossConfig.SERVER.armorBannerRequiredSticks.get(),
				() -> BetsyRossConfig.SERVER.armorBannerMaxWidth.get(),
				() -> BetsyRossConfig.SERVER.armorBannerMaxHeight.get(),
				() -> BetsyRoss.ARMOR_BANNER.get().getDefaultInstance()),
		FLAG_STANDARD(() -> BetsyRossConfig.SERVER.flagStandardRequiredSticks.get(),
				() -> BetsyRossConfig.SERVER.flagStandardMaxWidth.get(),
				() -> BetsyRossConfig.SERVER.flagStandardMaxHeight.get(),
				() -> BetsyRoss.FLAG_STANDARD.get().getDefaultInstance()),
		BANNER_STANDARD(() -> BetsyRossConfig.SERVER.bannerStandardRequiredSticks.get(),
				() -> BetsyRossConfig.SERVER.bannerStandardMaxWidth.get(),
				() -> BetsyRossConfig.SERVER.bannerStandardMaxHeight.get(),
				() -> BetsyRoss.BANNER_STANDARD.get().getDefaultInstance());

		private final Supplier<Integer> requiredSticks;
		private final Supplier<Integer> maxWidth;
		private final Supplier<Integer> maxHeight;
		private final Supplier<ItemStack> defaultInstance;

		TakenItem(Supplier<Integer> requiredSticks, Supplier<Integer> maxWidth, Supplier<Integer> maxHeight, Supplier<ItemStack> defaultInstance) {
			this.requiredSticks = requiredSticks;
			this.maxWidth = maxWidth;
			this.maxHeight = maxHeight;
			this.defaultInstance = defaultInstance;
		}

		public int getRequiredSticks() { return this.requiredSticks.get(); }
		public byte getMaxWidth() { return this.maxWidth.get().byteValue(); }
		public byte getMaxHeight() { return this.maxHeight.get().byteValue(); }

		public ItemStack getStack(EmbroideryTableMenu menu) {
			ItemStack result = this.defaultInstance.get();
			CompoundTag tag = result.getOrCreateTag();
			if (this == FLAG_BLOCK) {
				CompoundTag blockData = new CompoundTag();
				blockData.putString("FlagUrl", menu.url);
				blockData.putByte("Width", menu.width);
				blockData.putByte("Height", menu.height);
				tag.put("BlockEntityTag", blockData);
			} else {
				tag.putString("FlagUrl", menu.url);
				tag.putByte("Width", menu.width);
				tag.putByte("Height", menu.height);
			}
			return result;
		}
	}

	public class OutputSlot extends SlotItemHandler {
		private final TakenItem mode;

		public OutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, TakenItem mode) {
			super(itemHandler, index, xPosition, yPosition);
			this.mode = mode;
		}

		@Override public boolean mayPlace(@NotNull ItemStack stack) { return false; }

		@Override
		public void onTake(Player player, ItemStack stack) {
			super.onTake(player, stack);
			EmbroideryTableMenu.this.onTakeResult(player, stack, this.mode);
		}

		@Override
		protected void onQuickCraft(ItemStack stack, int count) {
			super.onQuickCraft(stack, count);
		}

		@Override
		public boolean mayPickup(Player playerIn) {
			return super.mayPickup(playerIn) && EmbroideryTableMenu.this.mayPickup(playerIn, this.mode);
		}

		@Nullable
		@Override
		public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
			return super.getNoItemIcon();
		}

		public void propagate(boolean fill) {
			this.set(fill ? this.mode.getStack(EmbroideryTableMenu.this) : ItemStack.EMPTY);
		}

		public TakenItem mode() { return this.mode; }
	}

}
