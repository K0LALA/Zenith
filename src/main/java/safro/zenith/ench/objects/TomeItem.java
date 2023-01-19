package safro.zenith.ench.objects;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import safro.zenith.Zenith;
import safro.zenith.api.enchant.TableApplicableItem;
import safro.zenith.ench.EnchModule;
import safro.zenith.ench.table.IEnchantableItem;
import safro.zenith.util.ApotheosisUtil;

public class TomeItem extends BookItem implements IEnchantableItem, TableApplicableItem {

	final ItemStack rep;
	final EnchantmentCategory type;

	public TomeItem(Item rep, EnchantmentCategory type) {
		super(new Item.Properties().tab(Zenith.APOTH_GROUP));
		this.type = type;
		this.rep = new ItemStack(rep);
		EnchModule.TYPED_BOOKS.add(this);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (this.type == null) return EnchModule.TYPED_BOOKS.stream().filter(b -> b != this).allMatch(b -> !enchantment.canEnchant(new ItemStack(b)));
		return enchantment.category == this.type || ApotheosisUtil.canApplyEnchantment(enchantment, this.rep);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(Component.translatable("info.zenith." + Registry.ITEM.getKey(this).getPath()).withStyle(ChatFormatting.GRAY));
		if (stack.isEnchanted()) {
			tooltip.add(Component.translatable("info.zenith.tome_error").withStyle(ChatFormatting.RED));
		}
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		return !stack.isEnchanted() ? super.getRarity(stack) : Rarity.UNCOMMON;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.isEnchanted()) {
			ItemStack book = new ItemStack(Items.ENCHANTED_BOOK, stack.getCount());
			EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(stack), book);
			return InteractionResultHolder.consume(book);
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public ItemStack onEnchantment(ItemStack stack, List<EnchantmentInstance> enchantments) {
		stack = new ItemStack(Items.ENCHANTED_BOOK);
		for (EnchantmentInstance inst : enchantments) {
			EnchantedBookItem.addEnchantment(stack, inst);
		}
		return stack;
	}

	@Override
	public boolean forciblyAllowsTableEnchantment(ItemStack stack, Enchantment enchantment) {
		return this.canApplyAtEnchantingTable(stack, enchantment);
	}
}