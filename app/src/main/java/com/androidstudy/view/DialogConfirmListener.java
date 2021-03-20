package com.androidstudy.view;

import lombok.NonNull;

@FunctionalInterface
public interface DialogConfirmListener {
    void onSignUpFirebase(@NonNull String email, @NonNull String password);
}
