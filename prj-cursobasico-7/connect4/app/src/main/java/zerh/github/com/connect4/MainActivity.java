package zerh.github.com.connect4;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transitionseverywhere.Explode;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;


public class MainActivity extends AppCompatActivity {

    GridLayout boardContent;
    ViewGroup activityContent;

    LogicBoard logicBoard;
    RelativeLayout boardParent;

    Player player1, player2;

    TextView player1Counter;
    TextView player2Counter;

    MediaPlayer knob;
    MediaPlayer win;

    private static final int COLS_MAX = 7;
    private static final int ROWS_MAX = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player1 = new Player(1, "Player1", (ImageView) findViewById(R.id.player1));
        player2 = new Player(2, "Player2", (ImageView) findViewById(R.id.player2));

        activityContent = findViewById(R.id.activityContent);
        boardContent = findViewById(R.id.board_content);
        boardParent = findViewById(R.id.board_parent);
        player1Counter = findViewById(R.id.player1CounterText);
        player2Counter = findViewById(R.id.player2CounterText);


        knob = MediaPlayer.create(this, R.raw.knob);
        win = MediaPlayer.create(this, R.raw.win);

        startLogicBoard();

        renderBoard();
    }

    private void renderBoard(){
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if(i == 0) {
                    final ImageView iv = new ImageView(this);
                    iv.setTag(j);
                    iv.setImageResource(R.drawable.down_arrow);
                    iv.setClickable(true);
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TransitionManager.beginDelayedTransition(activityContent, new Rotate());
                            v.setRotation(v.getRotation() == 360 ? 0 : 360);

                            fillCell((int)v.getTag(), iv);
                        }
                    });
                    boardContent.addView(iv, new GridLayout.LayoutParams(GridLayout.spec(i), GridLayout.spec(j)));
                    continue;
                }
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.cell);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 140, 140, true);
                ImageView iv = new ImageView(this);
                iv.setTag(new Cell(true, i, j, null));
                iv.setImageBitmap(bMapScaled);
                GridLayout.Spec row = GridLayout.spec(i);
                GridLayout.Spec col = GridLayout.spec(j);
                boardContent.addView(iv, new GridLayout.LayoutParams(row, col));
            }
        }
    }

    private void fillCell(int col, View v){


        Pair<Boolean, Integer> result = verifyAvailability(col);
        if(!result.first) {
            Toast.makeText(this, "Empate", Toast.LENGTH_SHORT).show();
            return;
        }

        int row = result.second;
        int index = getIndex(result.second, col);


        Bitmap bMap = BitmapFactory
                .decodeResource(getResources(), (logicBoard.getCurrentPlayer().getId() == 1 ? R.drawable.cell_green : R.drawable.cell_red));
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 140, 140, true);

        knob.start();

        ImageView iv = new ImageView(this);
        iv.setImageBitmap(bMapScaled);

        Cell cell = new Cell(false, row, col, logicBoard.getCurrentPlayer());

        iv.setTag(cell);
        logicBoard.fillCell(cell);

        TransitionManager.beginDelayedTransition(boardContent, new Fade().setDuration(1000));

        boardContent.removeViewAt(index);
        boardContent.addView(iv, index);

        if(checkBoard(row, col)){
            animateNewGame(v);

            return;
        }

        logicBoard.setCurrentPlayer(logicBoard.getCurrentPlayer().getId() == 1 ? player2 : player1);
        animateCurrentPlayer();
    }

    private void animateCurrentPlayer(){

        if(logicBoard.getCurrentPlayer().getId() == 1) {
            player1.setCurrent(true);
            player2.setCurrent(false);
        } else {
            player2.setCurrent(true);
            player1.setCurrent(false);
        }
    }

    private void animateNewGame(View v){

        final Rect viewRect = new Rect();
        v.getGlobalVisibleRect(viewRect);
        Transition explode = new Explode().setEpicenterCallback(new Transition.EpicenterCallback() {
            @Override
            public Rect onGetEpicenter(Transition transition) {
                return viewRect;
            }
        });
        explode.setDuration(1000);
        TransitionManager.beginDelayedTransition(boardParent, explode);
        boardContent.removeAllViewsInLayout();
        renderBoard();
    }

    private void startLogicBoard(){
        logicBoard = new LogicBoard
                .Builder()
                .setRows(7)
                .setCols(7)
                .setCurrentPlayer(player1)
                .build();
    }


    private boolean checkBoard(int row, int col){
        boolean isConnectedFour = logicBoard.verifyBoard(row, col);
        if(isConnectedFour){
            startLogicBoard();
            win.start();
            Toast.makeText(this, "El jugador: " + logicBoard.getCurrentPlayer().getName() + " ha ganado la partida.", Toast.LENGTH_LONG).show();
            if(logicBoard.getCurrentPlayer().getId() == 1) {
                int count = Integer.parseInt(player1Counter.getText().toString());
                count += 1;
                player1Counter.setText(String.valueOf(count));
            } else {
                String count = String.valueOf(Integer.parseInt(player2Counter.getText().toString())+ 1);
                player2Counter.setText(count);
            }
            return true;
        }
        return false;
    }

    private int getIndex(int rowIndex, int columnIndex){
        return (COLS_MAX * rowIndex) + columnIndex;
    }

    private Pair<Boolean, Integer> verifyAvailability(int col){
        for (int row = ROWS_MAX-1; row > 0; row--) {
            View v = boardContent.getChildAt(getIndex(row, col));
            if (((Cell)v.getTag()).isFree()){
                return new Pair(true, row);
            }
        }
        return new Pair(false, null);
    }
}
