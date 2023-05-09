package astavie.lf.mixin;

import astavie.lf.ExampleMod;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class ExampleMixin {
    @Inject(at = @At("HEAD"), method = "loadWorld")
    private void init(CallbackInfo info) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        server.sendMessage(MutableText.of(new LiteralTextContent("Hello, World 2!")));
        server.sendMessage(MutableText.of(new LiteralTextContent("Hello, World 2!")));
    }
}