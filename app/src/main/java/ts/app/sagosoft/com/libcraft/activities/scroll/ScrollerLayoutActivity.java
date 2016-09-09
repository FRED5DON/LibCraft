package ts.app.sagosoft.com.libcraft.activities.scroll;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ts.app.sagosoft.com.libcraft.R;
import ts.app.sagosoft.com.libcraft.activities.BaseActivity;

/**
 * Created by fred on 16/8/29.
 */
public class ScrollerLayoutActivity extends BaseActivity {


    @InjectView(R.id.topView)
    Button topView;
    @InjectView(R.id.button_sticky)
    View buttonSticky;
    @InjectView(R.id.tv_main)
    TextView tvMain;

    public static Intent mkIntent(Context context) {
        Intent intent = new Intent(context, ScrollerLayoutActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_layout);
        ButterKnife.inject(this);

        String s="代码不到一百行，是不是很简单呀，实现的效果是类似果冻的颤动效果，来来来，凯子哥带你分析下代码实现。\n" +
                "    首先我们用的是OverScroller，因为和Scroller非常类似，而且增加了回弹支持，所以大部分情况下我们都可以使用OverScroller。我们在构造函数完成初始化，然后因为我们需要记录最开始的位置，在回弹的时候需要用，所以在onSizeChange()完成了起始坐标的初始化。为了完成拖拽功能，我们需要重写onTouch，然后在MOVE事件中，完成控件的位置移动，用offsetLeftAndRight和offsetTopAndBottom即可，参数是一个相对位移的距离，所以很简单就完成了控件跟随手指移动的效果。\n" +
                "    最后的效果当然是控件回弹，但是这里的回弹并不是用spingBack()完成，而是通过startScroll()完成，只要设置好当前的位置和我们需要位移的距离，然后记住invalidate一下，我们就可以去computeScroll()里面实际的改变控件的位置了，通过getCurrX()就可以获取到如果当前滚动应该的位置，所以setX()就OK啦，很简单是不是？不过要记住invalidate()，这样才能继续往下触发未完成的滚动操作。\n" +
                "    另外发现没，这个控件叫JellyTextView，就是果冻TextView，因为实现的是有来回颤动的效果，这个怎么实现呢？也很简单，设置一个BounceInterpolation就可以了，so easy~\n" +
                "    OK，其实现在大部分的Scroller的用法我们都用过了，还剩下一个OverScroll特有的spingBack()和fling()，我们先介绍一个spingBack的用法。\n" +
                "    springBack(int startX, int startY, int minX, int maxX, int minY, int maxY) \n" +
                "    看上面的参数，前两个是开始位置，是绝对坐标，minX和maxX是用来设定滚动范围的，也是绝对坐标范围，如果startX不在这个范围里面，比如大于maxX，就会触发computeScroll()，我们可以移动距离，最终回弹到maxX所在的位置，并返回true，从而完成后续的滚动效果，比minX小的话，就会回弹到minX，一样的道理。所以我们可以像上面代码里面一样，判断是否在范围内，在的话，就invalidate()一下，触发滚动动画，所以名字叫spingBack()，即回弹，在上面的视频里有演示效果。参照效果和代码，你应该能看明白用法";
        tvMain.setText(s);
    }

}
