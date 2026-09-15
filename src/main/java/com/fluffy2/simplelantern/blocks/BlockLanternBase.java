package com.fluffy2.simplelantern.blocks;

import com.fluffy2.simplelantern.ModConfig;
import com.fluffy2.simplelantern.SimpleLantern;
import com.fluffy2.simplelantern.init.ModBlocks;
import com.fluffy2.simplelantern.init.ModItems;
import com.fluffy2.simplelantern.init.ModSounds;
import com.fluffy2.simplelantern.items.LanternHelper;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Общая база для двух состояний блока-фонаря.
 * В 1.12.2 логичнее было бы сделать один блок со свойством LIT,
 * но чтобы порт совпадал со старыми мирами по структуре — оставлены два блока.
 */
public abstract class BlockLanternBase extends BlockContainer {

    protected static final AxisAlignedBB LANTERN_AABB =
            new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.6875D, 0.6875D);

    protected BlockLanternBase(String name) {
        super(Material.IRON);
        setRegistryName(SimpleLantern.MODID, name);
        setUnlocalizedName(SimpleLantern.MODID + "." + name);
        setHardness(0.5F);
        setSoundType(SoundType.METAL);
        setCreativeTab(null);
    }

    public abstract boolean isLit();

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityLantern();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        // BlockContainer по умолчанию возвращает INVISIBLE — без этого блок не видно
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return LANTERN_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityLantern)) {
            return false;
        }
        TileEntityLantern lantern = (TileEntityLantern) te;

        if (player.isSneaking()) {
            // Шифт + ПКМ — забрать фонарь в руку
            if (!world.isRemote) {
                ItemStack drop = new ItemStack(ModItems.LANTERN_OFF);
                LanternHelper.setLantern(drop, lantern.getOil(), lantern.getReflect());
                world.setBlockToAir(pos);
                if (!player.inventory.addItemStackToInventory(drop)) {
                    player.dropItem(drop, false);
                }
            }
            return true;
        }

        if (!world.isRemote) {
            if (isLit()) {
                lantern.switchTo(ModBlocks.LANTERN_OFF);
                world.playSound(null, pos, ModSounds.LANTERN_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
            } else if (lantern.getOil() > 0 || !ModConfig.lanternRequireFuel) {
                lantern.switchTo(ModBlocks.LANTERN_ON);
                world.playSound(null, pos, ModSounds.LANTERN_ON, SoundCategory.BLOCKS, 1.0F, 1.0F);
            } else {
                player.sendMessage(new TextComponentTranslation("message.simplelantern.no_fuel"));
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(ModItems.LANTERN_OFF);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (!world.isRemote && te instanceof TileEntityLantern) {
            TileEntityLantern lantern = (TileEntityLantern) te;
            ItemStack drop = new ItemStack(ModItems.LANTERN_OFF);
            LanternHelper.setLantern(drop, lantern.getOil(), lantern.getReflect());
            spawnAsEntity(world, pos, drop);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, IBlockAccess world,
                         BlockPos pos, IBlockState state, int fortune) {
        // дроп выдаётся в breakBlock, чтобы сохранить NBT фонаря
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn() {
        return null;
    }
}
