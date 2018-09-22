package zerh.github.com.connect4;

import android.view.ViewGroup;
import android.widget.ImageView;

public class Player {
    private int id;
    private String name;
    private ImageView imageView;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Player(int id, String name, ImageView imageView) {
        this.id = id;
        this.name = name;
        this.imageView = imageView;
    }

    public void setCurrent(boolean current){
        ViewGroup.LayoutParams paramsPlayer = imageView.getLayoutParams();
        if(current) {
            paramsPlayer.width = 140;
            paramsPlayer.height = 140;
        } else {
            paramsPlayer.width = 200;
            paramsPlayer.height = 200;
        }
        imageView.setLayoutParams(paramsPlayer);
    }
}
