package base_mixins.client;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiChat;

import com.oldturok.turok.commands.TurokChatController;
import com.oldturok.turok.util.Wrapper;
import com.oldturok.turok.TurokChat;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiChat.class)
public abstract class MixinChat {
	@Shadow
	protected GuiTextField inputField;

	@Shadow
	public String historyBuffer;

	@Shadow
	public int sentHistoryCursor;

	@Shadow
	public abstract void initGui();

	@Inject(method = "Lnet/minecraft/client/gui/GuiChat;keyTyped(CI)V", at = @At("RETURN"))
	public void returnKeyTyped(char typedChar, int keyCode, CallbackInfo info) {
		if (!(Wrapper.getMinecraft().currentScreen instanceof GuiChat) || Wrapper.getMinecraft().currentScreen instanceof TurokChatController) {
			return;
		}

		if (inputField.getText().startsWith(TurokChat.get_prefix())) {
			Wrapper.getMinecraft().displayGuiScreen(new TurokChatController(inputField.getText(), historyBuffer, sentHistoryCursor));
		}
	}
}
