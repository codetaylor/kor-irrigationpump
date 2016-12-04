package com.sudoplay.mc.korirrigationpump.module.irrigationpump.irrigationpump;

import com.sudoplay.mc.kor.spi.network.KorMessageTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 12/3/2016.
 */
public class PacketIrrigationPumpAction extends
    KorMessageTileEntity<TileEntityIrrigationPump> implements
    IMessageHandler<PacketIrrigationPumpAction, IMessage> {

  private List<BlockPos> blockPosList;

  @SuppressWarnings("unused")
  public PacketIrrigationPumpAction() {
    // serialization
  }

  public PacketIrrigationPumpAction(TileEntityIrrigationPump tileEntity, List<BlockPos> blockPosList) {
    super(tileEntity);
    this.blockPosList = blockPosList;
  }

  public List<BlockPos> getBlockPosList() {
    return this.blockPosList;
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);

    int size = this.blockPosList.size();
    buf.writeInt(size);

    for (BlockPos blockPos : this.blockPosList) {
      buf.writeLong(blockPos.toLong());
    }
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);

    int size = buf.readInt();
    this.blockPosList = new ArrayList<>(size);

    for (int i = 0; i < size; i++) {
      this.blockPosList.add(BlockPos.fromLong(buf.readLong()));
    }
  }

  @Override
  public IMessage onMessage(PacketIrrigationPumpAction message, MessageContext ctx) {
    TileEntityIrrigationPump tileEntity = message.getTileEntity(Minecraft.getMinecraft().theWorld);

    if (tileEntity != null) {
      tileEntity.onMessage(message, ctx);
    }
    return null;
  }
}
