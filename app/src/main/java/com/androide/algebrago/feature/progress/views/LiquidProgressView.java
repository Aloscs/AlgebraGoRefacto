package com.androide.algebrago.feature.progress.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Vista personalizada que muestra el progreso mediante un efecto de líquido animado.
 *
 * Dibuja un círculo con:
 * <ul>
 *   <li>Un borde sólido.</li>
 *   <li>Un relleno tipo "agua" que sube según el porcentaje de progreso.</li>
 *   <li>Una onda animada en el borde del agua para dar sensación de movimiento.</li>
 *   <li>El porcentaje numérico centrado sobre el círculo.</li>
 * </ul>
 *
 * Uso en XML: incluir como {@code <com.androide.algebrago.feature.progress.views.LiquidProgressView>}
 * en el layout de {@code item_progress_block}.
 */
public class LiquidProgressView extends View {

    private Paint borderPaint, waterPaint, textPaint;
    private float progress = 0f;
    private float waveOffset = 0f;

    /**
     * Constructor requerido por el sistema de layouts de Android.
     *
     * @param context contexto de la aplicación.
     * @param attrs   atributos XML de la vista.
     */
    public LiquidProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(Color.parseColor("#111111"));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(6f);

        waterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        waterPaint.setColor(Color.parseColor("#B79CFF"));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#111111"));
        textPaint.setTextSize(34f);
        textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(a -> {
            waveOffset = (float) a.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    /**
     * Actualiza el nivel de progreso y redibuja la vista.
     * El valor se recorta automáticamente al rango [0.0, 1.0].
     *
     * @param p progreso como fracción entre 0.0 (vacío) y 1.0 (completo).
     */
    public void setProgress(float p) {
        progress = Math.max(0f, Math.min(1f, p));
        invalidate();
    }

    /**
     * Dibuja el círculo, el nivel de líquido animado y el porcentaje centrado.
     *
     * @param canvas lienzo proporcionado por el sistema para renderizar la vista.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float size = Math.min(getWidth(), getHeight());
        float radius = size / 2f - 6f;
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;

        Path clip = new Path();
        clip.addCircle(cx, cy, radius, Path.Direction.CW);

        canvas.save();
        canvas.clipPath(clip);

        float waterLevel = cy + radius - (progress * radius * 2f);

        Path wave = new Path();
        wave.moveTo(0, getHeight());

        for (int x = 0; x <= getWidth(); x++) {
            double angle = (x * 0.05) + (waveOffset * Math.PI * 2);
            float y = (float) (waterLevel + Math.sin(angle) * 10);
            wave.lineTo(x, y);
        }

        wave.lineTo(getWidth(), getHeight());
        wave.close();

        canvas.drawPath(wave, waterPaint);
        canvas.restore();

        canvas.drawCircle(cx, cy, radius, borderPaint);

        String pct = (int)(progress * 100) + "%";
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float textY = cy - (fm.ascent + fm.descent) / 2;
        canvas.drawText(pct, cx, textY, textPaint);
    }
    /**
     * Cambia el color del líquido animado.
     *
     * @param hexColor color en formato hexadecimal (ej. "#B79CFF").
     */
    public void setLiquidColor(String hexColor) {
        waterPaint.setColor(Color.parseColor(hexColor));
        invalidate();
    }
}