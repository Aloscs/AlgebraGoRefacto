package com.androide.algebrago.shared.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.androide.algebrago.feature.exercise.ExerciseViewModel;
import com.androide.algebrago.feature.feedback.FeedbackViewModel;
import com.androide.algebrago.feature.home.MainViewModel;

/**
 * Factory para crear ViewModels que requieren un parámetro Application.
 *
 * El framework de ViewModel por defecto solo instancia ViewModels sin parámetros.
 * AndroidViewModel ya incluye Application en su constructor, pero igualmente
 * se necesita esta Factory cuando se quiere pasar parámetros adicionales.
 *
 * Uso en Activity:
 *   ExerciseViewModel vm = new ViewModelProvider(
 *       this,
 *       new ViewModelFactory(getApplication())
 *   ).get(ExerciseViewModel.class);
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;

    public ViewModelFactory(@NonNull Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ExerciseViewModel.class)) {
            return (T) new ExerciseViewModel(application);
        }
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(application);
        }
        if (modelClass.isAssignableFrom(FeedbackViewModel.class)) {
            return (T) new FeedbackViewModel(application);
        }
        throw new IllegalArgumentException("ViewModel desconocido: " + modelClass.getName());
    }
}
