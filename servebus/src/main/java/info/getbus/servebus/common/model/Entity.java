package info.getbus.servebus.common.model;

import lombok.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Entity {
    @Nullable
    Long getId();
    void setId(@Nonnull @NonNull Long id);
}
