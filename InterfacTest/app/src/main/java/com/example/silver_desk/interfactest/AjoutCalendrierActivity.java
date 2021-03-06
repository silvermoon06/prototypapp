package com.example.silver_desk.interfactest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.silver_desk.interfactest.database.Calendrier;
import com.example.silver_desk.interfactest.database.Evenement;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import petrov.kristiyan.colorpicker.ColorPicker;

import static com.example.silver_desk.interfactest.HomeActivity.DATABASE;
import static com.example.silver_desk.interfactest.HomeActivity.actionBarDrawerToggle;
import static com.example.silver_desk.interfactest.HomeActivity.drawerLayout;

public class AjoutCalendrierActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    // les compoansant manipuler dans ce fragmant
    Spinner spinner_priorite ;

    EditText et_titre,et_description ;
    CheckBox c_activite,c_visibilite ;
    Button b_couleur ;
    FloatingActionButton fab_add_cal ,fab_delete_cal ;
    ArrayAdapter<CharSequence> arrayAdapter ;
    TextView t_titreajout;
    Evenement event;

    Time td,tf;
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_calendrier);
        // action bar


        // le drawer menu

        drawerLayout=(DrawerLayout)findViewById(R.id.ajout_cal_drawer);
        actionBarDrawerToggle= new ActionBarDrawerToggle(this ,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView =(NavigationView)findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);


        et_titre=(EditText)findViewById(R.id.et_titre);
        et_description=(EditText)findViewById(R.id.et_descritpion);

        b_couleur=(Button) findViewById(R.id.b_couleur);

        c_visibilite=(CheckBox)findViewById(R.id.c_visibilite);
        c_visibilite.setChecked(true);

        fab_add_cal=(FloatingActionButton)findViewById(R.id.fab_save_event) ;
        fab_add_cal.setOnClickListener(this);

        fab_delete_cal =(FloatingActionButton)findViewById(R.id.fab_delete_cal);
        fab_delete_cal.setOnClickListener(this);
        fab_delete_cal.setVisibility(View.INVISIBLE);



        b_couleur.setOnClickListener(this);
        b_couleur.setBackgroundColor(getResources().getColor(R.color.color3));
        // une couleure pardefaut


        // configuration du spinner
        arrayAdapter=ArrayAdapter.createFromResource(this,R.array.priorite,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // action bar
        getSupportActionBar().setTitle(getString(R.string.title_activity_ajout__calendrier));

        // texte view
        t_titreajout=(TextView)findViewById(R.id.t_titreajout);


        viewSetInfo();
    }
// action bar
    @Override
    public boolean onCreateOptionsMenu(Menu  menu) {
        getMenuInflater().inflate(R.menu.nav_lmenu,menu);
        return super.onCreateOptionsMenu(menu);

    }
   // action bar  onclik


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true ;
        }
        int id = item.getItemId();
        switch (id){
            case R.id.close_action :
                backCalendrierActivity();
            return  true ;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.fab_save_event){
         Boolean modification ;
            if(verifyIncomingIntent()) {  modification= true ;} else { modification=false ;}

            if (modification==true){
                // modification
              updatCalendrier(getincomingInten_idCalendrier());
              backCalendrierActivity();
            }else {
                // Ajout
                if(et_titre.getText().toString().equals("")) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Erreur de saisis");
                    builder.setIcon(R.drawable.ic_announcement_black_24dp);
                    builder.setMessage("vous n'avez pas saisie toutes les informations nécessaires");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{

                   Calendrier calendrier = new Calendrier();
                    inserCalendrier(calendrier);
                    backCalendrierActivity();
                }
            }


        }
        if (view.getId()==R.id.b_couleur){
            ColorPicker colorPicker=new ColorPicker(this);
            colorPicker.setRoundColorButton(true);
            colorPicker.show();
            colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                @Override
                public void onChooseColor(int position, int color) {

                    b_couleur.setBackgroundColor(color);
                }

                @Override
                public void onCancel() {

                }
            });
        }

        if (view.getId()==R.id.fab_delete_cal){

            if (verifyIncomingIntent()) {
                final Calendrier calendrier = DATABASE.calendrierDao().selecCalendrierById(getincomingInten_idCalendrier());


                AlertDialog.Builder deleteDailog = new AlertDialog.Builder(this);



                View view1= getLayoutInflater().inflate(R.layout.suppression_dialoge,null);
                Button b_oui,b_non;
                final CheckBox cb_transfert;
                final Spinner spinner ;
                // traitment sur la vue
                cb_transfert=(CheckBox) view1.findViewById(R.id.cb_transfert);

                spinner=(Spinner)view1.findViewById(R.id.spinner_list_cal) ;
                spinner.setVisibility(View.INVISIBLE);
                List<String>listcal=DATABASE.calendrierDao().loadAllCalendrierTitelsIfvisibel();

                ArrayAdapter arrayAdapter= new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,enlevéUnTitre(listcal,calendrier.getTitre()));
                spinner.setAdapter(arrayAdapter);

                cb_transfert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cb_transfert.isChecked()){
                            spinner.setVisibility(View.VISIBLE);

                        }else{
                            spinner.setVisibility(View.INVISIBLE);
                        }


                    }
                });

                deleteDailog.setView(view1);
                deleteDailog.setIcon(R.drawable.ic_event_black_24dp);
                deleteDailog.setTitle(getString(R.string.deleteTitleDialogCalendar));
                deleteDailog.setMessage(getString(R.string.deleteMessageDialogCalendar) + calendrier.getTitre()+" ?");
                // positive button(confirm and delet) s supprimer le calendrier et les evenments de ce calendrier
                deleteDailog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // on verifie si lutilisateur veux transferer ses evenment vers un autre calendrier
                        if (cb_transfert.isChecked()){
                         // recuperer le tirtre du calen,drier destination
                       String  titre_cal_destination  =  spinner.getSelectedItem().toString();
                         // recuperer lid du calendrier grace au titre
                        int id_cal_destination = DATABASE.calendrierDao().getIdCalendrierByTitel(titre_cal_destination);
                        Calendrier calendrier_destination = DATABASE.calendrierDao().selecCalendrierById(id_cal_destination);

                            transfertEvenment(calendrier,calendrier_destination);
                            DATABASE.calendrierDao().deletCalendrier(calendrier);
                            dialogInterface.dismiss();
                            backCalendrierActivity();

                        }else{
                            DATABASE.calendrierDao().deletCalendrier(calendrier);
                            backCalendrierActivity();
                        }


                    }
                });


                // negative button  (dont delet)
                deleteDailog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
                AlertDialog alertDialog = deleteDailog.create();
                alertDialog.show();
            }



        }


    }
    // verifier si on a ressu un intent
    private boolean verifyIncomingIntent(){
        if (getincomingInten_idCalendrier()!=0)
            return  true;
        else
            return false;
    }


    // recuperer  id du calendrier selectioner apartire de l intent
    private int getincomingInten_idCalendrier() {
        if (getIntent().hasExtra("id_cal")) {
            int id_cal = getIntent().getIntExtra("id_cal", 1);

            return id_cal;
        }
        return 0;
    }

    //initialiser la vue en cas de modification
    private void viewSetInfo(){
        if (verifyIncomingIntent()){
            // le titre de" la vue
            getSupportActionBar().setTitle("Edition calendrier ");
            t_titreajout.setText(R.string.setcalendar);
           Calendrier calendrier= DATABASE.calendrierDao().selecCalendrierById(getincomingInten_idCalendrier());
         // init titre
           et_titre.setText(calendrier.getTitre().toString());
         // init visibilité
            c_visibilite.setChecked(calendrier.isVisibilite());


         // init priorite

            String[] stringArray=getResources().getStringArray(R.array.priorite);
            List<String> stringList = new ArrayList<String>();
            stringList= stringArrayToList(stringArray);

         // init couleur
            b_couleur.setBackgroundColor(calendrier.getCouleur());

         fab_delete_cal.setVisibility(View.VISIBLE);



        }

    }

    private List<String> stringArrayToList(String[] stringArray) {
        List<String> stringList=new ArrayList<String>() ;
        for (int i=0 ;i < stringArray.length ;i++){
            stringList.add(i,stringArray[i]);
        }


        return stringList ;
    }

    private int getPositionOfitemByValu(String valu,List<String> stringList){
        int position = 0 ;
        for (int i=0 ;i < stringList.size();i++){
            // si un element de la liste correspond a la valeure
            // return his position
            if (stringList.get(i).equals(valu)){
                position=i;

            }

        }
        return position;


    }

    // inserer un calendrier
    private void inserCalendrier(Calendrier calendrier){
        // recuperer la couleure
       // GradientDrawable gradient=(GradientDrawable) b_couleur.getBackground();
        ColorDrawable b_couleurBackground=(ColorDrawable) b_couleur.getBackground();
        // recuperation des information du calendrier
        calendrier.setTitre(et_titre.getText().toString());
        calendrier.setVisibilite(c_visibilite.isChecked());
        calendrier.setDescription(et_description.getText().toString());
        calendrier.setCouleur(b_couleurBackground.getColor());
        DATABASE.calendrierDao().insert(calendrier);
        Toast.makeText(this, getString(R.string.addSuccess), Toast.LENGTH_SHORT).show();
    }
    // modifier un calendrier
    private void updatCalendrier (int id_cal){

        ColorDrawable b_couleurBackground=(ColorDrawable) b_couleur.getBackground();

      String titer =et_titre.getText().toString();

      Boolean visibilite=c_visibilite.isChecked();

      String description =et_description.getText().toString();
      int couleur=b_couleurBackground.getColor();
        // creation de lobjet

        Calendrier calendrier= new Calendrier(id_cal,titer,visibilite,couleur,description) ;
       DATABASE.calendrierDao().upDateCalendrier(calendrier);
        Toast.makeText(this, getString(R.string.updateSuccess), Toast.LENGTH_SHORT).show();


    }

    // retourner  calendrier  Activity
    private void backCalendrierActivity(){
        Intent intent = new Intent(this, CalendrierActivity.class);

        startActivity(intent);
    }

    public void transfertEvenment(Calendrier calendrier1 , Calendrier calendrier2){


            // modifier la valeuridcalendrie dans les evenment contenus dans cette liste

            // id du nouv calendrier parent
            int id_newParentCal= calendrier2.getId();
            // id de lencien calendrier parent
            int id_oldParentCal=calendrier1.getId();
            // modification
            DATABASE.evenementDao().transfertEventsToCalendrier(id_oldParentCal,id_newParentCal);




    }

    public List<String> enlevéUnTitre(List<String> stringList,String s){

        for (int i= 0 ;i<stringList.size();i++){
            if (stringList.get(i).equals(s)){
                stringList.remove(i);
            }

        }

        return stringList ;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //affichage du dashboard
        if (id==R.id.db){
            Toast.makeText(this,getString(R.string.home),Toast.LENGTH_LONG).show();
            goHome();
        }
        //affichage du calendrier
        if (id==R.id.calendrier){
            Toast.makeText(this,getString(R.string.calendar),Toast.LENGTH_LONG).show();
            goCalendrierActivity();
        }

        return false;
    }

    public void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    //open calendrier activity
    public void goCalendrierActivity() {
        Intent intent_calendrier = new Intent(this, CalendrierActivity.class);
        startActivity(intent_calendrier);

    }
    public void showDialogIfEmplty(EditText editText){
        if (editText.getText().equals("")){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Erreur de saisis");
            builder.setMessage("vous n'avez pas saisie toutes les informations nécessaire");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
