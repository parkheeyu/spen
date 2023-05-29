package com.example.myapplication10;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private CanvasView canvasView;

    private float pressureValue = 0.0f;
    private static final int WRITE_REQUEST_CODE = 1001;
    private static final int IMAGE_SAVE_REQUEST_CODE = 1002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String ncs = intent.getStringExtra("ncs");
        String genuine = intent.getStringExtra("genuine");
        String num = intent.getStringExtra("num");

        canvasView = new CanvasView(this);
        setContentView(canvasView);


        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // CanvasView를 생성하여 프레임 레이아웃에 추가합니다.
        canvasView = new CanvasView(this);
        frameLayout.addView(canvasView);

        // TextView를 생성하여 프레임 레이아웃에 추가합니다.
        TextView textView = new TextView(this);
        textView.setLayoutParams(new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        textView.setText("Force");
        textView.setTextSize(34);
        textView.setTextColor(Color.BLACK);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        layoutParams.topMargin = -800; // 수정된 부분
        textView.setLayoutParams(layoutParams);
        frameLayout.addView(textView);


        //로드 버튼 객체 생성
        Button loadButton = new Button(this);
        loadButton.setText("load");

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        FrameLayout.LayoutParams loadParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        loadParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        loadParams.bottomMargin = 1590; // 마진 추가
        loadButton.setLayoutParams(loadParams);


        // 파일 텍스트뷰 객체 생성
        TextView fileView = new TextView(this);
        fileView.setText("File : ");
        fileView.setTextSize(24);
        fileView.setTextColor(Color.BLACK);

        // 파일 텍스트뷰 레이아웃 파라미터 설정
        FrameLayout.LayoutParams fileParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        fileParams.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT; // 수정된 부분
        fileParams.bottomMargin = 550;
        fileParams.rightMargin = loadButton.getRight();
        fileParams.leftMargin = 200;
        fileView.setLayoutParams(fileParams);

        frameLayout.addView(fileView);
        frameLayout.addView(loadButton);


        // 리셋 버튼 객체 생성
        Button resetButton = new Button(this);
        resetButton.setText("clear");

        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        layoutParams2.bottomMargin = 400; // 마진 추가
        layoutParams2.leftMargin = -300; // 마진 추가
        resetButton.setLayoutParams(layoutParams2);


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.resetCanvas(); //CanvasView의 clearCanvas() 메소드 호출
                Toast.makeText(getApplicationContext(), "데이터를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        frameLayout.addView(resetButton);

//        // 클리어 버튼 객체 생성
//        Button clearButton = new Button(this);
//        clearButton.setText("Clear");
//
//        FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams3.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
//        layoutParams3.bottomMargin = 400; // 마진 추가
//        //layoutParams3.leftMargin = resetButton.getRight() + 0;
//        clearButton.setLayoutParams(layoutParams3);
//
//        clearButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                canvasView.clearCanvas(); //CanvasView의 clearCanvas() 메소드 호출
//                Toast.makeText(getApplicationContext(),"서명을 삭제하였습니다.",Toast.LENGTH_SHORT).show();
//            }
//        });
//        frameLayout.addView(clearButton);

        //세이브 버튼 객체 생성
        Button saveButton = new Button(this);
        saveButton.setText("Save");
        FrameLayout.LayoutParams saveParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        saveParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        saveParams.leftMargin = resetButton.getRight() + 300; // clear 버튼 오른쪽에 위치
        saveParams.bottomMargin = 400; // 마진 추가
        saveButton.setLayoutParams(saveParams);
        frameLayout.addView(saveButton);


        saveButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                try {
                    // CanvasView에서 그려진 그림을 가져옵니다.
                        Intent saveImageIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                        saveImageIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        saveImageIntent.setType("image/png");
                        saveImageIntent.putExtra(Intent.EXTRA_TITLE, ncs + "_" + genuine + "_" + num);

                        startActivityForResult(saveImageIntent, IMAGE_SAVE_REQUEST_CODE);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "이미지 저장 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
                try {


                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TITLE, ncs + "_" + genuine + "_" + num );
                    startActivityForResult(intent, WRITE_REQUEST_CODE);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        setContentView(frameLayout);
    }

    // onActivityResult 메소드에서 선택한 이미지를 가져와서 캔버스에 그려줍니다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_SAVE_REQUEST_CODE) {
                // 이미지 저장 결과 처리
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    try {
                        Bitmap canvasBitmap = canvasView.getCanvasBitmap();
                        OutputStream outputStream = getContentResolver().openOutputStream(imageUri);
                        if (outputStream != null) {
                            outputStream.close();
                            canvasBitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.close();
                            Toast.makeText(getApplicationContext(), "이미지가 저장되었습니다.", Toast.LENGTH_SHORT).show();}
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "이미지 저장 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == WRITE_REQUEST_CODE) {
                Uri uri = data.getData();
                if (uri != null) {
                    try {
                        OutputStream outputStream = getContentResolver().openOutputStream(uri);
                        if (outputStream != null) {
                            String filename = "pressure.txt";
                            pressureValue = canvasView.getLastPressure();
                            String content = "Pressure: " + pressureValue;
                            outputStream.write(content.getBytes());
                            outputStream.close();
                            Toast.makeText(getApplicationContext(), "필압 값이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == 1) {
                try {
                    // 선택한 이미지를 Bitmap으로 가져옵니다.
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();

                    // 가져온 이미지를 캔버스에 그려줍니다.
                    canvasView.drawImage(imageBitmap);
                    // 캔버스 뷰를 업데이트합니다.
                    canvasView.invalidate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private class CanvasView extends View {
        private Path path = new Path();
        private Paint paint = new Paint();
        private float lastX, lastY;
        private RectF boxRect; // 박스의 좌표를 저장할 변수
        private Bitmap imageBitmap;
        private RectF imageRect;
        private ImageView imageView;
        private Paint mPaint;
        private Path mPath;

        private boolean mIsEraserMode = false;
        private SurfaceView mSurfaceView;
        private SurfaceHolder mSurfaceHolder;
        private float mLastPressure = 0.0f;
        private float mLastX = 0.0f;
        private float mLastY = 0.0f;


        public CanvasView(Context context) {

            super(context);

            mPath = new Path();

            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            //paint.setStrokeWidth(10f);

            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(10);



        }
        public float getLastPressure() {
            return mLastPressure;
        }

        public CanvasView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public void drawImage(Bitmap imageBitmap) {

            int desiredSize = (int) Math.min(boxRect.width(), boxRect.height()); // 원하는 크기 값
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, desiredSize, desiredSize, false);
            this.imageBitmap = scaledBitmap;
            invalidate();
        }


        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            // View가 생성될 때 박스의 좌표를 계산합니다.
            int boxSize = Math.min(w, h) / 2; // 박스의 크기를 View의 절반 크기로 설정합니다.
            int left = (w - boxSize) / 2;
            int top = (h - boxSize) / 2;
            int right = left + boxSize;
            int bottom = top + boxSize;
            boxRect = new RectF(left, top, right, bottom);


        }



        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // 정사각형 박스
            paint.setColor(Color.LTGRAY);
            paint.setStrokeWidth(10f);
            canvas.drawRect(boxRect, paint);
            //그림 그리기
            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(calculateStrokeWidth(mLastPressure));
            canvas.drawPath(mPath, mPaint);


            if (imageBitmap != null) {
                int centerX = (int) boxRect.centerX();
                int centerY = (int) boxRect.centerY();
                int halfWidth = imageBitmap.getWidth() / 2;
                int halfHeight = imageBitmap.getHeight() / 2;

                if (imageView == null) {
                    // 크기가 조정된 비트맵으로 이미지 보기 만들기
                    imageView = new ImageView(getContext());
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    // 보기 계층에 이미지 보기 추가

                    ((ViewGroup) getParent()).addView(imageView);
                }
                imageView.setAlpha(128);
                imageView.setImageBitmap(imageBitmap);

                // 상자 내에서 ImageView의 중심을 맞추기 위한 좌표를 계산합니다
                int left = centerX - halfWidth;
                int top = centerY - halfHeight;
                int right = centerX + halfWidth;
                int bottom = centerY + halfHeight;
                RectF imageRect = new RectF(left, top, right, bottom);
                imageView.setX(imageRect.left);
                imageView.setY(imageRect.top);

            }

        }

        private void resetCanvas() {
            mPath.reset();
            if (imageBitmap != null) {
                imageBitmap.recycle();
                imageBitmap = null;
            }
            if (imageView != null) {
                ((ViewGroup) getParent()).removeView(imageView);
                imageView = null;
            }
            invalidate();
        }

        private void clearCanvas() {
            mPath.reset();
            invalidate();
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int pointerCount = event.getPointerCount();

            for (int i = 0; i < pointerCount; i++) {
                float x = event.getX(i);
                float y = event.getY(i);
                float pressure = event.getPressure(i);

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        // 박스 내부를 터치했는지 확인합니다.
                        if (boxRect.contains(x, y)) {
                            mPath.moveTo(x, y);
                            lastX = x;
                            lastY = y;
                            return true;
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // 박스 내부에서만 그리도록 합니다.
                        if (!boxRect.contains(x, y)) {
                            break;
                        }

                        int historySize = event.getHistorySize();
                        for (int h = 0; h < historySize; h++) {
                            float historicalX = event.getHistoricalX(i, h);
                            float historicalY = event.getHistoricalY(i, h);
                            float historicalPressure = event.getHistoricalPressure(i, h);

                            // 필압 값 갱신
                            mLastPressure = historicalPressure;

                            mPath.quadTo(lastX, lastY, historicalX, historicalY);
                            lastX = historicalX;
                            lastY = historicalY;
                        }

                        // 필압 값 갱신
                        mLastPressure = pressure;

                        mPath.quadTo(lastX, lastY, x, y);
                        lastX = x;
                        lastY = y;

                        invalidate();
                        break;
                }
            }

            lastX = event.getX();
            lastY = event.getY();
            invalidate();
            return true;
        }

        private float calculateStrokeWidth(float pressure) {
            float minPressure = 0.1f; // 최소 필압 값
            float maxPressure = 1.0f; // 최대 필압 값
            float minStrokeWidth = 2.0f; // 최소 선의 두께
            float maxStrokeWidth = 20.0f; // 최대 선의 두께

            // 필압 값의 범위를 선의 두께 값의 범위에 매핑합니다.
            float normalizedPressure = Math.max(minPressure, Math.min(maxPressure, pressure));
            float normalizedStrokeWidth = ((normalizedPressure - minPressure) / (maxPressure - minPressure)) * (maxStrokeWidth - minStrokeWidth) + minStrokeWidth;

            return normalizedStrokeWidth;
        }


        public Bitmap getCanvasBitmap() {
            Bitmap canvasBitmap = Bitmap.createBitmap(canvasView.getWidth(), canvasView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas tempCanvas = new Canvas(canvasBitmap);
            canvasView.draw(tempCanvas);
            return canvasBitmap;
        }

    }
}




