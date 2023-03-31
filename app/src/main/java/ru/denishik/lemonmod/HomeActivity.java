package ru.denishik.lemonmod;

import android.Manifest;
import android.content.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.net.Uri;
import android.os.*;
import android.os.Bundle;
import android.util.*;
import android.view.*;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.io.*;
import java.io.InputStream;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.DownloadManager;


public class HomeActivity extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private HashMap<String, Object> libraryMap = new HashMap<>();
    private Gridview1Adapter adapter;
    private String pathVrchat = "";
    private Gridview1Adapter adapterInstaled;

    private ArrayList<HashMap<String, Object>> libraryList = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> listMods = new ArrayList<>();
    private ArrayList<String> instaledModsList = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> instaledModsListM = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> spinnerData = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> reposList = new ArrayList<>();

    private LinearLayout linear1;
    private LinearLayout linear2;
    private ImageView imageview4;
    private ImageView imageview1;
    private LinearLayout linear3;
    private LinearLayout linear7;
    private ImageView imageview2;
    private Spinner spinner1;
    private TextView textview1;
    private TextView textview4;
    private LinearLayout block_error;
    private GridView gridview1;
    private LinearLayout linear5;
    private CardView cardview1;
    private LinearLayout linear4;
    private TextView textview2;
    private TextView textview3;
    private ImageView imageview5;

    private DatabaseReference mods = _firebase.getReference("lemonMod/mods");
    private ChildEventListener _mods_child_listener;
    private Intent i = new Intent();

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.home);
        initialize(_savedInstanceState);
        FirebaseApp.initializeApp(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        } else {
            initializeLogic();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            initializeLogic();
        }
    }

    private void initialize(Bundle _savedInstanceState) {
        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        imageview4 = findViewById(R.id.imageview4);
        imageview1 = findViewById(R.id.imageview1);
        linear3 = findViewById(R.id.linear3);
        linear7 = findViewById(R.id.linear7);
        imageview2 = findViewById(R.id.imageview2);
        spinner1 = findViewById(R.id.spinner1);
        textview1 = findViewById(R.id.textview1);
        textview4 = findViewById(R.id.textview4);
        block_error = findViewById(R.id.block_error);
        gridview1 = findViewById(R.id.gridview1);
        linear5 = findViewById(R.id.linear5);
        cardview1 = findViewById(R.id.cardview1);
        linear4 = findViewById(R.id.linear4);
        textview2 = findViewById(R.id.textview2);
        textview3 = findViewById(R.id.textview3);
        imageview5 = findViewById(R.id.imageview5);

        imageview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                _showRepos();
            }
        });

        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                _showLibrary();
            }
        });

        imageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                _showAuthor();
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                final int _position = _param3;
                if (_position == 0) {
                    gridview1.setAdapter(adapter);
                } else {
                    _updateList();
                    gridview1.setAdapter(adapterInstaled);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> _param1) {

            }
        });

        textview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                _showAuthor();
            }
        });

        gridview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                final int _position = _param3;

            }
        });

        imageview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://vrchat.denishik.ru/vrcat"));
                startActivity(i);
            }
        });

        _mods_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (_childValue != null && FileUtil.isExistFile(FileUtil.getExternalStorageDir().concat("/android/data/com.vrchat.oculus.quest/files/Mods".concat("")))) {
                    int num = String.valueOf(_childValue.get("download")).split("/").length;
                    String file = String.valueOf(_childValue.get("download")).split("/")[num - 1];
                    _childValue.put("file", file);
                    listMods.add(_childValue);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        mods.addChildEventListener(_mods_child_listener);
    }

    private void initializeLogic() {
        if (FileUtil.isExistFile(FileUtil.getExternalStorageDir().concat("/android/data/com.vrchat.oculus.quest/files/Mods".concat("")))) {
            libraryMap = new HashMap<>();
            libraryMap.put("image", "https://noznet.ru/wp-content/uploads/2021/12/cx-provodnik-dlya-android-menedzher-fajlov-s-rut-dostupom-i-udobnym-predstavleniem-informaczii.png");
            libraryMap.put("name", "CX manager");
            libraryMap.put("path", "cx.apk");
            libraryList.add(libraryMap);
            libraryMap = new HashMap<>();
            libraryMap.put("image", "https://noznet.ru/wp-content/uploads/2021/12/cx-provodnik-dlya-android-menedzher-fajlov-s-rut-dostupom-i-udobnym-predstavleniem-informaczii.png");
            libraryMap.put("name", "MelonLoader installer");
            libraryMap.put("path", "ml.apk");
            libraryList.add(libraryMap);
            adapter = new Gridview1Adapter(listMods);
            gridview1.setAdapter(adapter);
            pathVrchat = FileUtil.getExternalStorageDir().concat("/android/data/com.vrchat.oculus.quest/files/Mods".concat(""));
            pathVrchat = "/android/data/com.vrchat.oculus.quest/files/Mods".concat("");
            FileUtil.listDir(FileUtil.getExternalStorageDir().concat("/android/data/com.vrchat.oculus.quest/files/Mods".concat("")), instaledModsList);
            for (int i = 0; i < (int) (instaledModsList.size()); i++) {
                libraryMap = new HashMap<>();
                int num = String.valueOf(instaledModsList.get((int) (i))).split("/").length;

                libraryMap.put("name", String.valueOf(instaledModsList.get((int) (i))).split("/")[num - 1]);
                instaledModsListM.add(libraryMap);
            }
            adapterInstaled = new Gridview1Adapter(instaledModsListM);
            libraryMap = new HashMap<>();
            libraryMap.put("title", "All");
            spinnerData.add(libraryMap);
            libraryMap = new HashMap<>();
            libraryMap.put("title", "Installed");
            spinnerData.add(libraryMap);
            spinner1.setAdapter(new Spinner1Adapter(spinnerData));
            libraryMap = new HashMap<>();
            libraryMap.put("name", "Default repo");
            libraryMap.put("image", "https://vrchat.denishik.ru/static/media/worldsBG.d75753825047bca8a2e0.png");
            libraryMap.put("id", "default");
            reposList.add(libraryMap);
        } else {
            spinner1.setVisibility(View.GONE);
            gridview1.setVisibility(View.GONE);
            block_error.setVisibility(View.VISIBLE);
        }
    }

    public void _showLibrary() {
        final com.google.android.material.bottomsheet.BottomSheetDialog dialog = new com.google.android.material.bottomsheet.BottomSheetDialog(HomeActivity.this);
        View lay = getLayoutInflater().inflate(R.layout.library_view, null);
        dialog.setContentView(lay);
        final LinearLayout linear1 = (LinearLayout) lay.findViewById(R.id.linear1);

        final ListView listLibrary = lay.findViewById(R.id.list_library);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
        android.graphics.drawable.GradientDrawable wd = new android.graphics.drawable.GradientDrawable();
        wd.setColor(Color.WHITE);
        wd.setCornerRadius((int) 10f);
        linear1.setBackground(wd);

        listLibrary.setAdapter(new LlistLibraryAdapter(libraryList));

    }

    public class LlistLibraryAdapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> _data;

        public LlistLibraryAdapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = getLayoutInflater();
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.library_item, null);
            }

            final TextView name = _view.findViewById(R.id.name);

            final ImageView image = _view.findViewById(R.id.image);

            final ImageView install = _view.findViewById(R.id.install);

            final LinearLayout linear2 = _view.findViewById(R.id.linear2);

            name.setText(getItem(_position).get("name").toString());
            Glide.with(getApplicationContext()).load(Uri.parse(getItem(_position).get("image").toString())).into(image);
            linear2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    _openApk(getItem(_position).get("path").toString());
                }
            });

            return _view;
        }
    }

    public void _openApk(final String _name) {
        FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/lemonMod/apks"));

        AssetManager assetManager = getAssets();
        InputStream in = null;
        OutputStream out = null;
        String path = Environment.getExternalStorageDirectory().getPath() + "/lemonMod/apks/" + _name + "";
        File myAPKFile = new File(path);
        try {
            if (!myAPKFile.exists()) {
                in = assetManager.open("apks/".concat(_name));
                out = new FileOutputStream(myAPKFile);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            }
            OtherUtil.showMessage(getApplicationContext(), "file://".concat(path));
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("file://".concat(path)));
            startActivity(i);
        } catch (Exception e) {
        }
    }


    public HashMap<String, Object> _getHashMap(final HashMap<String, Object> _map, final String _key) {
        return ((HashMap<String, Object>) _map.get(_key));
    }


    public void _download(final String _url) {
        int num = String.valueOf(_url).split("/").length;
        String file = String.valueOf(_url).split("/")[num - 1];
        if (FileUtil.isFile(FileUtil.getExternalStorageDir().concat("/android/data/com.vrchat.oculus.quest/files/Mods/".concat(file)))) {
            OtherUtil.showMessage(getApplicationContext(), "you have the mod");
        } else {
            FileUtil.makeDir(pathVrchat);
            DownloadManager manager;
            manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(_url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setDestinationInExternalPublicDir(pathVrchat, file);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            long reference = manager.enqueue(request);
            adapter.notifyDataSetChanged();
            OtherUtil.showMessage(getApplicationContext(), "Install mod");
        }
    }


    public void _deleteMods(final String _name) {
        int pos = -1;
        for (int i = 0; i < (int) (instaledModsList.size()); i++) {
            if (_name.contains(instaledModsList.get((int) (i)))) {
                pos = i;
            }
        }
        if (!(pos == -1)) {
            FileUtil.deleteFile(_name);
            instaledModsList.remove((int) (pos));
            instaledModsListM.remove((int) (pos));
            adapter.notifyDataSetChanged();
            adapterInstaled.notifyDataSetChanged();
            OtherUtil.showMessage(getApplicationContext(), "deleted");
        }
    }


    public void _showAuthor() {
        final com.google.android.material.bottomsheet.BottomSheetDialog dialog = new com.google.android.material.bottomsheet.BottomSheetDialog(HomeActivity.this);
        View lay = getLayoutInflater().inflate(R.layout.author_view, null);
        dialog.setContentView(lay);
        final LinearLayout linear1 = (LinearLayout) lay.findViewById(R.id.linear1);

        final com.google.android.material.button.MaterialButton go = lay.findViewById(R.id.go);

        final com.google.android.material.button.MaterialButton go1 = lay.findViewById(R.id.go1);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
        android.graphics.drawable.GradientDrawable wd = new android.graphics.drawable.GradientDrawable();
        wd.setColor(Color.WHITE);
        wd.setCornerRadius((int) 10f);
        linear1.setBackground(wd);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://vrchat.denishik.ru"));
                startActivity(i);
            }
        });
        go1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://github.com/LemonLoader"));
                startActivity(i);
            }
        });
    }


    public void _showRepos() {
        final com.google.android.material.bottomsheet.BottomSheetDialog dialog = new com.google.android.material.bottomsheet.BottomSheetDialog(HomeActivity.this);
        View lay = getLayoutInflater().inflate(R.layout.repos_view, null);
        dialog.setContentView(lay);
        final LinearLayout linear1 = (LinearLayout) lay.findViewById(R.id.linear1);

        final ListView listview1 = lay.findViewById(R.id.listview1);

        final de.hdodenhof.circleimageview.CircleImageView add = lay.findViewById(R.id.add);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
        android.graphics.drawable.GradientDrawable wd = new android.graphics.drawable.GradientDrawable();
        wd.setColor(Color.BLACK);
        wd.setCornerRadius((int) 10f);
        linear1.setBackground(wd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                OtherUtil.showMessage(getApplicationContext(), "Coming soon");
            }
        });

        listview1.setAdapter(new Llistview1Adapter(reposList));

    }

    public class Llistview1Adapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> _data;

        public Llistview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = getLayoutInflater();
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.mod_item, null);
            }

            final TextView title = _view.findViewById(R.id.title);

            final ImageView image = _view.findViewById(R.id.image);

            final androidx.cardview.widget.CardView card = _view.findViewById(R.id.cardview2);

            title.setText(getItem(_position).get("name").toString());
            Glide.with(getApplicationContext()).load(Uri.parse(getItem(_position).get("image").toString())).into(image);
            title.setTextColor(0xFFFFFFFF);
            card.setCardBackgroundColor(0xFF000000);

            return _view;
        }
    }


    public void _updateList() {
        instaledModsList.clear();
        instaledModsListM.clear();
        FileUtil.listDir(FileUtil.getExternalStorageDir().concat("/android/data/com.vrchat.oculus.quest/files/Mods".concat("")), instaledModsList);
        for (int i = 0; i < (int) (instaledModsList.size()); i++) {
            libraryMap = new HashMap<>();
            int num = String.valueOf(instaledModsList.get((int) (i))).split("/").length;
            showMessage(String.valueOf(instaledModsList.get((int) (i))));

            libraryMap.put("name", String.valueOf(instaledModsList.get((int) (i))).split("/")[num - 1]);
            instaledModsListM.add(libraryMap);
        }
        adapterInstaled.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }


    public void onChangeViewActivity(final Configuration _conf) {
        int newOrientation = _conf.orientation;
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridview1.setNumColumns((int) 3);
        } else {
            gridview1.setNumColumns((int) 2);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onChangeViewActivity(newConfig);
    }

    public class Spinner1Adapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> _data;

        public Spinner1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = getLayoutInflater();
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.spinner_item, null);
            }

            final LinearLayout linear1 = _view.findViewById(R.id.linear1);
            final TextView text = _view.findViewById(R.id.text);

            text.setText(getItem(_position).get("title").toString());

            return _view;
        }
    }

    public class Gridview1Adapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> _data;

        public Gridview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = getLayoutInflater();
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.mod_item, null);
            }

            final LinearLayout linear3 = _view.findViewById(R.id.linear3);
            final androidx.cardview.widget.CardView cardview1 = _view.findViewById(R.id.cardview1);
            final LinearLayout linear1 = _view.findViewById(R.id.linear1);
            final ImageView image = _view.findViewById(R.id.image);
            final LinearLayout linear2 = _view.findViewById(R.id.linear2);
            final androidx.cardview.widget.CardView cardview2 = _view.findViewById(R.id.cardview2);
            final TextView title = _view.findViewById(R.id.title);

            try {
                Glide.with(getApplicationContext()).load(Uri.parse(getItem(_position).get("image").toString())).into(image);
                title.setText(getItem(_position).get("name").toString());
                if (FileUtil.isFile(FileUtil.getExternalStorageDir().concat("/android/data/com.vrchat.oculus.quest/files/Mods/".concat(getItem(_position).get("file").toString())))) {
                    title.setTextColor(0xFF4CAF50);
                }
                cardview1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View _view) {
                        _download(getItem(_position).get("download").toString());
                    }
                });
            } catch (Exception e) {
                title.setText(getItem(_position).get("name").toString());
                cardview1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View _view) {
                        OtherUtil.showMessage(getApplicationContext(), "Чтобы удалить, зажмите");
                    }
                });
                cardview1.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View _view) {
                        _deleteMods(FileUtil.getExternalStorageDir().concat("/android/data/com.vrchat.oculus.quest/files/Mods/".concat(getItem(_position).get("name").toString())));
                        return true;
                    }
                });
            }

            return _view;
        }
    }

    @Deprecated
    public void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

    @Deprecated
    public int getLocationX(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[0];
    }

    @Deprecated
    public int getLocationY(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[1];
    }

    @Deprecated
    public int getRandom(int _min, int _max) {
        Random random = new Random();
        return random.nextInt(_max - _min + 1) + _min;
    }

    @Deprecated
    public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
        ArrayList<Double> _result = new ArrayList<Double>();
        SparseBooleanArray _arr = _list.getCheckedItemPositions();
        for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
            if (_arr.valueAt(_iIdx))
                _result.add((double) _arr.keyAt(_iIdx));
        }
        return _result;
    }

    @Deprecated
    public float getDip(int _input) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
    }

    @Deprecated
    public int getDisplayWidthPixels() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Deprecated
    public int getDisplayHeightPixels() {
        return getResources().getDisplayMetrics().heightPixels;
    }
}
