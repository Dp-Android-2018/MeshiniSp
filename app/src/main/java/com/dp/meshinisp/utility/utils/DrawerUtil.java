package com.dp.meshinisp.utility.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.dp.meshinisp.R;
import com.dp.meshinisp.view.ui.activity.MainActivity;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import androidx.appcompat.widget.Toolbar;


public class DrawerUtil {
    public static void getDrawer(final Activity activity, Toolbar toolbar) {
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem drawerEmptyItem= new PrimaryDrawerItem().withIdentifier(0).withName("");
        drawerEmptyItem.withEnabled(false);

     /*   PrimaryDrawerItem drawerItemManagePlayers = new PrimaryDrawerItem().withIdentifier(1)
                .withName(R.string.manage_player).withIcon(R.drawable.ic_people_outline_black_48px);
        PrimaryDrawerItem drawerItemManagePlayersTournaments = new PrimaryDrawerItem()
                .withIdentifier(2).withName(R.string.tournament).withIcon(R.drawable.tournamenticon);

        SecondaryDrawerItem drawerItemSettings = new SecondaryDrawerItem().withIdentifier(3)
                .withName(R.string.settings).withIcon(R.drawable.ic_settings_black_48px);
        SecondaryDrawerItem drawerItemAbout = new SecondaryDrawerItem().withIdentifier(4)
                .withName(R.string.about).withIcon(R.drawable.ic_info_black_24px);
        SecondaryDrawerItem drawerItemHelp = new SecondaryDrawerItem().withIdentifier(5)
                .withName(R.string.help).withIcon(R.drawable.ic_help_black_24px);
        SecondaryDrawerItem drawerItemDonate = new SecondaryDrawerItem().withIdentifier(6)
                .withName(R.string.donate).withIcon(R.drawable.ic_payment_black_24px);*/





        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                        drawerEmptyItem,drawerEmptyItem,drawerEmptyItem,
//                        drawerItemManagePlayers,
//                        drawerItemManagePlayersTournaments,
                        new DividerDrawerItem()
//                        drawerItemAbout,
//                        drawerItemSettings,
//                        drawerItemHelp,
//                        drawerItemDonate
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    if (drawerItem.getIdentifier() == 2 && !(activity instanceof MainActivity)) {
                        // load tournament screen
                        Intent intent = new Intent(activity, MainActivity.class);
                        view.getContext().startActivity(intent);
                    }
                    return true;
                })
                .build();
    }
}
