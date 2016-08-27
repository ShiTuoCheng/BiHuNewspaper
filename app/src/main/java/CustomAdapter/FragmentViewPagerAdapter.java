package CustomAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shituocheng.bihunewspaper.com.bihunewspaper.LongCommentFragment;
import com.shituocheng.bihunewspaper.com.bihunewspaper.ShortCommentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shituocheng on 16/5/12.
 */
public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {

    int number;

    public FragmentViewPagerAdapter(FragmentManager fm, int number) {
        super(fm);
        this.number = number;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                LongCommentFragment longCommentFragment = new LongCommentFragment();
                return longCommentFragment;
            case 1:
                ShortCommentFragment shortCommentFragment = new ShortCommentFragment();
                return shortCommentFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return number;
    }
}
