package dev.hephaestus.clothy.impl.gui.entries;

import dev.hephaestus.clothy.api.AbstractConfigListEntry;
import dev.hephaestus.clothy.api.Tooltip;
import dev.hephaestus.math.impl.Point;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class TooltipListEntry<T> extends AbstractConfigListEntry<T> {
    
    @Nullable private Function<T, Optional<List<Text>>> tooltipSupplier;
    
    public TooltipListEntry(Text fieldName, @Nullable Function<T, Optional<List<Text>>> tooltipSupplier, Consumer<T> saveConsumer, Supplier<T> defaultValue) {
        this(fieldName, tooltipSupplier, false, saveConsumer, defaultValue);
    }
    
    public TooltipListEntry(Text fieldName, @Nullable Function<T, Optional<List<Text>>> tooltipSupplier, boolean requiresRestart, Consumer<T> saveConsumer, Supplier<T> defaultValue) {
        super(fieldName, requiresRestart, saveConsumer, defaultValue);
        this.tooltipSupplier = tooltipSupplier;
    }
    
    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        if (isMouseInside(mouseX, mouseY, x, y, entryWidth, entryHeight)) {
            Optional<List<Text>> tooltip = getTooltip();
            if (tooltip.isPresent() && tooltip.get().size() > 0)
                addTooltip(Tooltip.of(new Point(mouseX, mouseY), tooltip.get()));
        }
    }
    
    public Optional<List<Text>> getTooltip() {
        if (tooltipSupplier != null) {
            return tooltipSupplier.apply(this.getValue());
        }

        return Optional.empty();
    }
    
    @Nullable
    public Function<T, Optional<List<Text>>> getTooltipSupplier() {
        return tooltipSupplier;
    }
    
    public void setTooltipSupplier(@Nullable Function<T, Optional<List<Text>>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
    }
    
}
