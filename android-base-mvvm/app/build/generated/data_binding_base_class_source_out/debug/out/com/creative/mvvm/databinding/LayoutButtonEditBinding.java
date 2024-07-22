// Generated by view binder compiler. Do not edit!
package com.creative.mvvm.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import com.creative.mvvm.R;
import java.lang.NullPointerException;
import java.lang.Override;

public final class LayoutButtonEditBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  private LayoutButtonEditBinding(@NonNull CardView rootView) {
    this.rootView = rootView;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutButtonEditBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutButtonEditBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_button_edit, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutButtonEditBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    return new LayoutButtonEditBinding((CardView) rootView);
  }
}
