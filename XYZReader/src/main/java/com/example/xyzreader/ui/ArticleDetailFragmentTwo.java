//package com.example.xyzreader.ui;
//
//import android.app.Fragment;
//import android.app.LoaderManager;
//import android.content.Intent;
//import android.content.Loader;
//import android.content.res.ColorStateList;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.text.Html;
//import android.text.format.DateUtils;
//import android.text.method.LinkMovementMethod;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ShareCompat;
//import androidx.palette.graphics.Palette;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.DataSource;
//import com.bumptech.glide.load.engine.GlideException;
//import com.bumptech.glide.request.RequestListener;
//import com.bumptech.glide.request.target.Target;
//import com.example.xyzreader.R;
//import com.example.xyzreader.data.ArticleLoader;
//import com.google.android.material.appbar.AppBarLayout;
//import com.google.android.material.appbar.CollapsingToolbarLayout;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.Objects;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//public class ArticleDetailFragmentTwo extends android.app.Fragment implements
//        LoaderManager.LoaderCallbacks<Cursor> {
//
//    private static final String TAG = "ArticleDetailFragment";
//
//    public static final String ARG_ITEM_ID = "item_id";
//    private static final float PARALLAX_FACTOR = 1.25f;
//
//    private int mMutedColor = 0xFF333333;
//    private Cursor mCursor;
//    private long mItemId;
//    private View mRootView;
//
//    @BindView(R.id.share_fab)
//    FloatingActionButton mShareFab;
//    @BindView(R.id.detail_bar)
//    AppBarLayout mAppBarLayout;
//    @BindView(R.id.collapsing_bar)
//    CollapsingToolbarLayout mCollapsingToolbarLayout;
//    @BindView(R.id.detail_toolbar)
//    Toolbar mToolbar;
//    @BindView(R.id.photo)
//    ImageView mImageView;
//    @BindView(R.id.meta_bar)
//    LinearLayout mMetaBar;
//    @BindView(R.id.article_title)
//    TextView mTitle;
//    @BindView(R.id.article_byline)
//    TextView mByLine;
//    @BindView(R.id.article_body)
//    TextView mBody;
//
//    private SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
//    // Use default locale format
//    private SimpleDateFormat outputFormat = new SimpleDateFormat();
//    // Most time functions can only handle 1902 - 2037
//    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);
//
//    public ArticleDetailFragmentTwo() {
//    }
//
//    public static Fragment newInstance(long itemId) {
//        Bundle arguments = new Bundle();
//        arguments.putLong(ARG_ITEM_ID, itemId);
//        ArticleDetailFragmentTwo fragment = new ArticleDetailFragmentTwo();
//        fragment.setArguments(arguments);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        assert getArguments() != null;
//        if (getArguments().containsKey(ARG_ITEM_ID)) {
//            mItemId = getArguments().getLong(ARG_ITEM_ID);
//        }
//
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        getLoaderManager().initLoader(0, null, this);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mRootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
//        ButterKnife.bind(this, mRootView);
//        toolbarHelper();
//        return mRootView;
//    }
//
//    private void toolbarHelper() {
//        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
//        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);
//
//        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back, null));
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ArticleDetailFragmentTwo.this.getActivity().onBackPressed();
//            }
//        });
//    }
//
//    private Date parsePublishedDate() {
//        try {
//            String date = mCursor.getString(ArticleLoader.Query.PUBLISHED_DATE);
//            return dateFormat.parse(date);
//        } catch (ParseException ex) {
//            Log.e(TAG, ex.getMessage());
//            Log.i(TAG, "passing today's date");
//            return new Date();
//        }
//    }
//
//    private void bindViews() {
//        mByLine.setMovementMethod(new LinkMovementMethod());
//
//        final String title = mCursor.getString(ArticleLoader.Query.TITLE);
//        final String body = Html.fromHtml(mCursor.getString(ArticleLoader.Query.BODY)).toString();
//        final String photoUrl = mCursor.getString(ArticleLoader.Query.PHOTO_URL);
//        final String time = Html.fromHtml(
//                DateUtils.getRelativeTimeSpanString(
//                        mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
//                        System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
//                        DateUtils.FORMAT_ABBREV_ALL).toString()
//                        + " by "
//                        + mCursor.getString(ArticleLoader.Query.AUTHOR)).toString();
//
//        mTitle.setText(title);
//        mByLine.setText(time);
//
//        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = true;
//            int scrollRange = -1;
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                if (scrollRange + verticalOffset == 0) {
//                    mCollapsingToolbarLayout.setTitle(title);
//                    isShow = true;
//                    mMetaBar.setVisibility(View.GONE);
//                } else if(isShow) {
//                    mCollapsingToolbarLayout.setTitle(" ");
//                    isShow = false;
//                    mMetaBar.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//        mShareFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ArticleDetailFragmentTwo.this.startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(ArticleDetailFragmentTwo.this.getActivity())
//                        .setType("text/plain")
//                        .setText(title + "\n" + body)
//                        .getIntent(), ArticleDetailFragmentTwo.this.getString(R.string.action_share)));
//            }
//        });
//
//        prepareImage(photoUrl);
//    }
//    private void prepareImage(String photoUrl) {
//        Glide.with(this)
//                .asBitmap()
//                .load(photoUrl)
//                .listener(new RequestListener<Bitmap>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
//                            @Override
//                            public void onGenerated(@Nullable Palette palette) {
//                                assert palette != null;
//                                if (palette.getDominantSwatch() != null) {
//                                    mMetaBar.setBackgroundColor(palette.getDominantSwatch().getRgb());
//                                    mCollapsingToolbarLayout.setContentScrimColor(palette.getDominantSwatch().getBodyTextColor());
//                                    mCollapsingToolbarLayout.setStatusBarScrimColor(palette.getDominantSwatch().getRgb());
//                                    mShareFab.setBackgroundTintList(ColorStateList.valueOf(palette.getDarkMutedSwatch().getRgb()));
//                                }
//                            }
//                        });
//                        return false;
//                    }
//                })
//                .into(mImageView);
//    }
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        return ArticleLoader.newInstanceForItemId(getActivity(), mItemId);
//    }
//
//    @Override
//    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
//        if (!isAdded()) {
//            if (data != null) {
//                data.close();
//            }
//            return;
//        }
//
//        mCursor = data;
//        if (mCursor != null && !mCursor.moveToFirst()) {
//            mCursor.close();
//            mCursor = null;
//        }
//
//        bindViews();
//    }
//
//    @Override
//    public void onLoaderReset(android.content.Loader<Cursor> loader) {
//        mCursor = null;
//        bindViews();
//    }
//
//}
