package ts.app.sagosoft.com.libcraft.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ts.app.sagosoft.com.libcraft.R;
import ts.app.sagosoft.com.libcraft.model.HttpProperty;

/**
 * Created by FRED_angejia on 2016/3/3.
 */
public class ImageLoaderActivity extends BaseActivity implements View.OnClickListener {


    private static final String POSITION = "POSITION";
    @InjectView(R.id.image_container)
    LinearLayout imageContainer;

    final int offset = 8;
    private int position;
    private Thread thread;
    private LinkedList<String> templeList;

    public static Intent mkIntent(Context context, int position) {
        Intent intent = new Intent(context, ImageLoaderActivity.class);
        intent.putExtra(POSITION, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        ButterKnife.inject(this);
        position = getIntent().getIntExtra(POSITION, 0);
        templeList = new LinkedList<>();
        for (int i = 0; i < offset; i++) {
            if (position + i >= list.length) {
                break;
            }
            ImageView iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400);
            iv.setLayoutParams(layoutparams);
            ImageLoader.getInstance().displayImage(list[position + i], iv);
            templeList.add(list[position + i]);
            iv.setOnClickListener(this);
            imageContainer.addView(iv);
        }
    }

    @Override
    public void onClick(View v) {
        startActivity(ImageLoaderActivity.mkIntent(this, position + offset));
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImageLoader.getInstance().clearMemoryCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    String[] list = {
            "http://img.agjimg.com/Fj3MFW8-4HLBcfQBgf30vDrHCnW9",
            "http://img.agjimg.com/FrLNLbMiUGuqE_fzav8jXmhmhTwe",
            "http://img.agjimg.com/FjTiDD0LnSXpUoSpV_8tZh9qeNtw",
            "http://img.agjimg.com/FqugHWBDX9-B6qpprkwlI0GkJrw-",
            "http://img.agjimg.com/FhNbbKZvaQ6kVJJpfIgCvGDkLexC",
            "http://img.agjimg.com/Frbgfo0TJNwGfqVKk7gnxK90kvr4",
            "http://img.agjimg.com/FqEKYq8iyO76Y7A0QAw3Tz3xIkM8",
            "http://img.agjimg.com/FsbhZeYRDerNR_7e2a8UtyNfoNL3",
            "http://img.agjimg.com/Fo8eUl6VfZLeKjOOgkIDeU3wqXoW",
            "http://img.agjimg.com/Fn6VlzU9j03x1f8aLWtFotLTH4Oj",
            "http://img.agjimg.com/Fpe0M0oNMMOTAo-_AQF-b_h8zdRV",
            "http://img.agjimg.com/FmSDX_WD105AtGWMjJhsZiVTAH8l",
            "http://img.agjimg.com/FsRGjowQ8Qemj-S2Rj0k7iejIPDr",
            "http://img.agjimg.com/FvYR-TVgYsQ5-SngBerwlqbC06CW",
            "http://img.agjimg.com/FstRc2F8IktbME2On0BD1qZ9jkFJ",
            "http://img.agjimg.com/FsKTtlcsTqH0b29_34MXiEkgqvEz",
            "http://img.agjimg.com/FiOdRG6UU1IHVicJ5V5fT19jYN_7",
            "http://img.agjimg.com/Fjm8ln3NnQHTBgMvRocylOAiErTK",
            "http://img.agjimg.com/FmwH5CutHFqvv3sT2v3qjKlXNwrc",
            "http://img.agjimg.com/Fpyczi_1LRENeiTsu-tv5ky5zqzy",
            "http://img.agjimg.com/FnhYugfaGfI9WeRfNOE6uhiklVZe",
            "http://img.agjimg.com/Ftxex0jD5rXVOyyptmZxKvfiKW3J",
            "http://img.agjimg.com/FrW0l3qmGKdeExb01lPfdTmZJIOU",
            "http://img.agjimg.com/FiXHqOUKkbvxXupSlBXD-hvysnUM",
            "http://img.agjimg.com/FuMyQsLOK98nNKW-eA41e0WhM5aC",
            "http://img.agjimg.com/FuGdxOubZbR5cHhDMXlIoQlRNeIQ",
            "http://img.agjimg.com/FtAZWyVbywsyrEbSg_rMphuxMjzT",
            "http://img.agjimg.com/FoATPKU9SuCozy2VjGO-6dUxzI17",
            "http://img.agjimg.com/Fo0wDJFiqslyldjar78lyMCx1NMr",
            "http://img.agjimg.com/FsltZpuFMwV-Jml8HAEw2YPvxlxq",
            "http://img.agjimg.com/Fm5-RU5hN_e8unCKdiKOXEPziHgT",
            "http://img.agjimg.com/Fp4FwYzTRb6-FEmIjtQiN1sw2C3y",
            "http://img.agjimg.com/Fi5FCTeDIDNFXSLteolq64-pdW44",
            "http://img.agjimg.com/FivUDzqNVOOCNNwfScRv0C6dfE5H",
            "http://img.agjimg.com/FlvMdJ9VLzyW8yCY5lI7Blj29RrU",
            "http://img.agjimg.com/Fg6zc448k3pqY57f0F8_Hq5UVL-R",
            "http://img.agjimg.com/Fn4DAoAaN76UdbdnuBppp8iPwnVM",
            "http://img.agjimg.com/FhvBRSQ6HyomaEgEe-pKrO9S_0RR",
            "http://img.agjimg.com/Fu20zunb7ZueNe6HYjNuaF1WEuIH",
            "http://img.agjimg.com/FrIsv_Qp1ADDKUmZtBdm3R5t5S3-",
            "http://img.agjimg.com/Fg3QTDFalqeorrzgrmUHW_LRYkY-",
            "http://img.agjimg.com/FkI-DQnp82Q7znsXlPl5dycFWVk-",
            "http://img.agjimg.com/Fpb9_4nkh8ka4UCYst6JYYfLv2X8",
            "http://img.agjimg.com/FiQkl7069aOJuh9ixw9ZvCzl7KWH",
            "http://img.agjimg.com/Fv3quRK5VwsO6Nz-18JCHaHx_-rg",
            "http://img.agjimg.com/FnhABv_aZ2IUxeHGWJLFTqmlgl3N",
            "http://img.agjimg.com/FiDoL2sGdMzKtPQqaSQ9QImw0I3P",
            "http://img.agjimg.com/Fp4FwYzTRb6-FEmIjtQiN1sw2C3y",
            "http://img.agjimg.com/Fi5FCTeDIDNFXSLteolq64-pdW44",
            "http://img.agjimg.com/FuHTan8TmkaDkRpliSGKGY0hk3jz",
            "http://img.agjimg.com/FknObPxWpYyCRiaApaU5q9SKsEqh",
            "http://img.agjimg.com/FoATPKU9SuCozy2VjGO-6dUxzI17",
            "http://img.agjimg.com/Fv2kzz9j4vsf60xY_UvF9FWk4ORu",
            "http://img.agjimg.com/FvY57ms0un9v7TJAAaJYwWXvcPuI",
            "http://img.agjimg.com/Fg0gqorwRq69azwUJkMUYJEzgYWb",
            "http://img.agjimg.com/FvAEk0yovgV2ON_-6BcJ3HM3ZcBn",
            "http://img.agjimg.com/FgBe1Qv7RqK_luyxC6vihasqpP0_",
            "http://img.dev.agjimg.com/Fl5hwxEIEcko_LEm80CfKIJ1UvQq",
            "http://img.agjimg.com/FnO2QsUNBR7C_65d2gBGyCcPBtE4",
            "http://img.agjimg.com/Fim7J7daKmAKlHgSHe_XdrB2p94c",
            "http://img.agjimg.com/Fqk1yqXqbf8vUE5lz-etg7-tLSdf",
            "http://img.agjimg.com/FpaSeWvjVj-fRdnc5xBUKykaUH2-",
            "http://img.agjimg.com/Fpt_MmY_dZ3a02dJ0maptHnZbcs-",
            "http://img.agjimg.com/FqFDl-56x-g27AWNYEX-l3-bl01c",
            "http://img.agjimg.com/FnU08LYzPMdZ4kJj5tvFeKSzJPWh",
            "http://img.dev.agjimg.com/FhGQ-lnjEAuL5Nyok2YlzR4FAakn",
            "http://img.dev.agjimg.com/FhwGVmnVmEJf9RsTvW6HZzLEWZ6J",
            "http://img.dev.agjimg.com/Fh__qFrHKIfbXnHhxSh-d_YZDBsa",
            "http://img.dev.agjimg.com/FhwGVmnVmEJf9RsTvW6HZzLEWZ6J",
            "http://img.dev.agjimg.com/FgUi-08z9FBuJvqp9d5UC6TrFvO8",
            "http://img.agjimg.com/FiumE-a8WAVO5cFvqK_wHm72ly_p",
            "http://img.dev.agjimg.com/FrAAxntnjOG3g3Ginn5hv3SWH2js",
            "http://img.agjimg.com/Fml2oI3HLl1vqSEFZxLdP0SUPvi3",
            "http://img.agjimg.com/FqOyKT3ntj_77xThQZOZ3liOk4Z4",
            "http://img.agjimg.com/FqwHeN7m_b3_1J41wWaOKlCtBWfj",
            "http://img.agjimg.com/FhlMnDvawKpjc2zhvNlS8MFMIOzE",
            "http://img.dev.agjimg.com/FlaYdsqte5I7YPduvrB9Bshj-w_z",
            "http://img.agjimg.com/Fsg-7s69cB3PETUR40soY94y6z_E",
            "http://open.agjimg.com/FlqdfHWM8F9pyBtMf_gzMh4kFgdt.png",
            "http://img.agjimg.com/Fj3MFW8-4HLBcfQBgf30vDrHCnW9",
            "http://img.agjimg.com/Fj8ZeMp3L8YwO3rPMe9icHI6wuzM",
            "http://img.agjimg.com/FoDnn6LW1850kG0_t8jIfVLqKdYx",
            "http://img.agjimg.com/FrHD5yPDQToCZJuE_4d6DpZ5ztrj",
            "http://img.dev.agjimg.com/FgUi-08z9FBuJvqp9d5UC6TrFvO8",
            "http://img.dev.agjimg.com/FlaYdsqte5I7YPduvrB9Bshj-w_z",
            "http://img.agjimg.com/FmtODcoEAVKRFN8_F4W8HYiWvoBW",
            "http://img.agjimg.com/FvRITagRu5pgsYVcRLO1J2xNZH3E",
            "http://img.agjimg.com/FgQHC0USxj2F4Aqg8_YeGJ7bPSPR",
            "http://img.agjimg.com/FnaLKSpL3X39DXaEypUNMPQmV99d",
            "http://img.agjimg.com/FjsiU8Aw9Lig1vlOda7Uvc9Th1rf",
            "http://img.agjimg.com/FqC7fSbl8uxhzVx-wiAeur3k0nHG",
            "http://img.agjimg.com/Flo-6rvqZV4nsnyBgeSyEaK7VpJz",
            "http://img.agjimg.com/Fv9XE7ibvaZ7QxeucaGQFoXGEXmS",
            "http://img.agjimg.com/FsPLghxs3IlW7S53y8djrozE2Jmk",
            "http://img.agjimg.com/FgY15QX2wlCj3LWemWVjPwr314Td",
            "http://img.agjimg.com/FvQjua-0WDS11tfaK5QdW1JUO84Z",
            "http://img.agjimg.com/FmrSJVF6wJ_g-CrH1Oonbn2q_Ri_",
            "http://img.agjimg.com/FiJ-MPwYjwR_O2yWvKFARmjPWeS_",
            "http://img.agjimg.com/FmtlpECVGfLytn90z-YJZkDMNCSc",
            "http://img.agjimg.com/Fi4AHCOn1QQacp-RUbPSch7vn8tJ",
            "http://img.agjimg.com/Fh3-zo9Sc77BZbcgINtwg925SN3v",
            "http://img.agjimg.com/FqAy19PfACdnjpdJ9Epg9YMMUhm-",
            "http://img.agjimg.com/Fk7yTxLiLo0kqwAvo1ydkXymafjp",
            "http://img.agjimg.com/Fpkr1ZhPCx5MqsWsIKKCzbZ5p5cL",
            "http://img.agjimg.com/Fm9yYsempkiJdBNy4-5JQ5-UGyKq",
            "http://img.agjimg.com/FsRSn5UWWSufqoZOQ0yncfSZmDKx",
            "http://img.agjimg.com/FpO5BDsXIrLAYVc2j3ElUkpTCljc",
            "http://img.agjimg.com/Fsml75E807KJFJM2_mUQ8z5Qs767",
            "http://img.agjimg.com/FoxS4zBBjMh27qdd7AJfos4tE5Q3",
            "http://img.agjimg.com/FiTLgc7XNrwNzuUPaVzreNlnxf7P",
            "http://img.agjimg.com/FsK790DFE5Do1gaPUFRkyvZn_l8Q",
            "http://img.agjimg.com/FsEoLOH9Euem_xMAC1C_UtJrCe86",
            "http://img.agjimg.com/Fp8nkEODvrGGpcLtkb6di2lSGSNv",
            "http://img.agjimg.com/Fnd0Tg1725YmLp05rUE7M1L6QFwx",
            "http://img.agjimg.com/FngousXi3VzIKCgpqFokbYaKV-FJ",
            "http://img.agjimg.com/FsHpb-LuN51h2oHr_J51cvX-4Rrm",
            "http://img.agjimg.com/FmTFXDAIY8hyt1VMjgcaBjuRNhSr",
            "http://img.agjimg.com/Fj1Oix7JOmJpiCn_lKjFapZUd7PU",
            "http://img.agjimg.com/FhfCd-UB2waBBTNrosisdBIplGbl",
            "http://img.agjimg.com/FmjLFmQBZLqjhMBgk3hCaIOCZyCl",
            "http://img.agjimg.com/FsenQlOFfpBBB18WPoIEy3qx6y-S",
            "http://img.agjimg.com/Fi_pg0fXPYvhN8vyXzJ7Md1-HT0P",
            "http://img.agjimg.com/FlWXyKx3FyTKq3SqUGNqVLOoXRzV",
            "http://img.agjimg.com/FoNIP8fpsrAnBdxzWKpk1wBULD6U",
            "http://img.agjimg.com/Fq8Sc_lq5now5i3xtpmPEzrx-8gb",
            "http://img.agjimg.com/FmjtsKOqImaTZVOk1Q4p5S1v8Wc-",
            "http://img.agjimg.com/FucJTBQx982vhEXum5GYx3jNNYZS",
            "http://img.agjimg.com/Fpb28LZmfU5sn11VVzeIMFXOcx-C",
            "http://img.dev.agjimg.com/Fh2iNGkUIjFjQWELRcXCPw0sIPIV",
            "http://img.agjimg.com/FjWx-uUlp9fe08ex-XZOfwRZD0FC",
            "http://img.dev.agjimg.com/FrAAxntnjOG3g3Ginn5hv3SWH2js",
            "http://img.dev.agjimg.com/FlaYdsqte5I7YPduvrB9Bshj-w_z",
            "http://img.agjimg.com/FmSbdDEpc8gEzKe6NJcLD26efZyY",
            "http://img.agjimg.com/Fmt13G0gm3Ukgp6GnIVdp62aW45M",
            "http://img.agjimg.com/Fof5Ixelh8bzdjQzj_QA3KuqtcOa",
            "http://img.agjimg.com/Ftal1KPhfq42reMCuxtkXQhZ0AUd",
            "http://img.agjimg.com/FqwHeN7m_b3_1J41wWaOKlCtBWfj",
            "http://img.agjimg.com/FhlMnDvawKpjc2zhvNlS8MFMIOzE",
            "http://img.agjimg.com/Fsg-7s69cB3PETUR40soY94y6z_E",
            "http://img.agjimg.com/FpIeKLg2BqKcsqx-cyvlYWGDyO0m",
            "http://img.dev.agjimg.com/FlaYdsqte5I7YPduvrB9Bshj-w_z",
            "http://img.agjimg.com/Fj8ZeMp3L8YwO3rPMe9icHI6wuzM",
            "http://img.dev.agjimg.com/FhGQ-lnjEAuL5Nyok2YlzR4FAakn",
            "http://img.agjimg.com/FjeOxkatwBBGoMALhPosSLvkAPMc",
            "http://img.agjimg.com/FtrRRKNiDBmfjkoZT6yKgtaKhsSO",
            "http://img.agjimg.com/FjBrACV4tsORsF6QQ-0GHxj2hweF",
            "http://img.agjimg.com/FpUDfGswakCbh9gzMb28U1p4M2GS",
            "http://img.agjimg.com/FvmrftBDOLKOLlvSzNWZ3fIgz88u",
            "http://img.agjimg.com/FnSGcBJ-5y7G9od3o26Tbjumfz9G",
            "http://img.agjimg.com/FlCtWSo7EjNj0z_Kt3ofCqfTDIIj",
            "http://img.agjimg.com/FlhnpL8Dqf9iNGVwqt-SExgEOEuS",
            "http://img.agjimg.com/FrURXBVk5eKR6X5Mbf77PjViMpwH",
            "http://img.agjimg.com/FkWugBfvzy7tLfVV646Enp0GHyFb",
            "http://img.agjimg.com/Fge5K7HTLshTpNtYCj5Axw25TqHw",
            "http://img.agjimg.com/FkX9YVakv-w85XSqJ-WatICn93W7",
            "http://img.agjimg.com/FmtZo8SYIW5hPu8DBf4CgJF6KMtB",
            "http://img.agjimg.com/FtuKx6VveygXe6O9YV5YVcA76R12",
            "http://img.agjimg.com/Fq_RaoiR9vkCzyxG0edTm3U4_ylQ",
            "http://img.agjimg.com/FqwHeN7m_b3_1J41wWaOKlCtBWfj",
            "http://img.agjimg.com/FhlMnDvawKpjc2zhvNlS8MFMIOzE",
            "http://img.dev.agjimg.com/FlaYdsqte5I7YPduvrB9Bshj-w_z",
            "http://img.agjimg.com/Fsg-7s69cB3PETUR40soY94y6z_E",
            "http://img.dev.agjimg.com/FhGQ-lnjEAuL5Nyok2YlzR4FAakn"
    };

}
