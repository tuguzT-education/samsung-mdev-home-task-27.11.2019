package ru.pavlenty.duckhunting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;


public class GameView extends View {

    public static int DELTA_TIME = 100;
    private int [ ] TARGETS = { R.drawable.anim_duck0, R.drawable.anim_duck1,
            R.drawable.anim_duck2, R.drawable.anim_duck1 };
    private Paint paint;
    private Bitmap[ ] ducks;
    private int duckFrame;

    private Game game;

    public GameView(Context context, int width, int height ) {
        super( context );
        ducks = new Bitmap[TARGETS.length];
        for( int i = 0; i < ducks.length; i++ )
            ducks[i] =
                    BitmapFactory.decodeResource( getResources( ), TARGETS[i] );
        float scale = ( ( float ) width / ( ducks[0].getWidth( ) * 5 ) );
        Rect duckRect = new Rect( 0, 0, width / 5,
                ( int ) ( ducks[0].getHeight( ) * scale ) );
        game = new Game( duckRect, 5, .03f, .2f );
        game.setDuckSpeed( width * .00003f );
        game.setBulletSpeed( width * .0003f );
        game.setDeltaTime( DELTA_TIME );

        game.setHuntingRect( new Rect( 0, 0, width, height ) );
        game.setCannon( new Point( width / 10, height - 200 ), width / 30,
                width / 15, width / 50);

        paint = new Paint( );
        paint.setColor(Color.BLACK);
        paint.setAntiAlias( true );
        paint.setStrokeWidth( game.getBarrelRadius( ) );
    }

    public void onDraw( Canvas canvas ) {
        super.onDraw( canvas );
        // рисуем ружье

        canvas.drawCircle( game.getCannonCenter( ).x, game.getCannonCenter( ).y,
                game.getCannonRadius( ), paint );

        // рисуем дуло ружья
        canvas.drawLine(
                game.getCannonCenter( ).x, game.getCannonCenter( ).y,
                game.getCannonCenter( ).x + game.getBarrelLength( )
                        * ( float ) Math.cos( game.getCannonAngle( ) ),
                game.getCannonCenter( ).y - game.getBarrelLength( )
                        * ( float ) Math.sin( game.getCannonAngle( ) ),
                paint );

        // рисуем пулю
        if( ! game.bulletOffScreen( ) )
            canvas.drawCircle( game.getBulletCenter( ).x,
                    game.getBulletCenter( ).y, game.getBulletRadius( ), paint );

        // рисуем утку
        duckFrame = ( duckFrame + 1 ) % ducks.length;
        if( game.isDuckShot( ) )
            canvas.drawBitmap( ducks[0], null,
                    game.getDuckRect( ), paint );
        else
            canvas.drawBitmap( ducks[duckFrame], null,
                    game.getDuckRect( ), paint );
    }

    public Game getGame( ) {
        return game;
    }
}
