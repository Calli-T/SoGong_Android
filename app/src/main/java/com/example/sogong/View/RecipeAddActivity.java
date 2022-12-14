package com.example.sogong.View;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlRecipe_f;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.Model.Recipe_Ingredients;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.Inflater;

public class RecipeAddActivity extends AppCompatActivity {
    Context context;

    EditText recipetitle;
    Spinner recipecate;
    Spinner recipespicy;
    EditText recipedescription;

    Button addingre;
    LinearLayout linearlayout1;
    LinearLayout linearlayout;
    TextView ingrename;
    TextView ingreamount;
    TextView ingreunit;
    Spinner ingrename_spinner;
    TextView ingrename_selectText;
    EditText ingreamount_edit;
    TextView ingreamount_text;
    ImageButton back_button;
    private AtomicBoolean threadFlag = new AtomicBoolean();
    String[] spicy_int = new String[]{"0", "1", "2", "3", "4", "5", "????????? ??????"};
    HashMap<String, String> unitmap = new HashMap<>();//????????? ?????? ????????? ?????? map
    Dialog ingreSelectDialog;

    public static int responseCode;
    public static RecipePost_f newRecipe = new RecipePost_f();//????????? ????????????????????? ????????? ?????? ??????

    List<Recipe_Ingredients> recipe_ingredients = new ArrayList<>();//????????? ????????? ?????? ??????
    List<String> existName = new ArrayList<>();

    int ingre_cnt;//????????? ???

    Boolean isEdit;
    RecipePost_f recipePostF;
    RecipeAdd_UI rau = new RecipeAdd_UI();
    Custon_ProgressDialog custon_progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrecipe);
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        recipePostF = getIntent().getParcelableExtra("EditRecipe");

        context = RecipeAddActivity.this;
        recipetitle = findViewById(R.id.recipetitle_text1);
        recipecate = findViewById(R.id.recipecate_spinner);
        recipespicy = findViewById(R.id.recipespicy_spinner);
        addingre = findViewById(R.id.ingreadd_btn);
        recipedescription = findViewById(R.id.recipedescription_edit);
        linearlayout = findViewById(R.id.ingre_linear);//????????????????????? ????????????
        back_button = findViewById(R.id.back_button);

        //???????????? ??????
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //???????????????
        FloatingActionButton fab = findViewById(R.id.recipe_add_button);
        fab.setOnClickListener(new FABClickListener());//??????????????? ?????? ?????????

        ingre_cnt = 0;//????????? ???

        //????????? ?????? ?????????
        SpinnerWithHintAdapter spinnerArrayAdapter1 = new SpinnerWithHintAdapter(context, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.category));
        spinnerArrayAdapter1.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        recipecate.setAdapter(spinnerArrayAdapter1);
        recipecate.setSelection(spinnerArrayAdapter1.getCount());

        //?????? ??? ?????? ?????????
        SpinnerWithHintAdapter spinnerArrayAdapter2 = new SpinnerWithHintAdapter(context, android.R.layout.simple_spinner_dropdown_item, spicy_int);
        spinnerArrayAdapter2.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        recipespicy.setAdapter(spinnerArrayAdapter2);
        recipespicy.setSelection(spinnerArrayAdapter2.getCount());

        //????????? ??????
        custon_progressDialog = new Custon_ProgressDialog(this);
        custon_progressDialog.setCanceledOnTouchOutside(false);

        //?????? ?????? ??? ?????? ?????? ui ?????? ??????
        addingre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("????????? ??????", "button click");
                ingreSelectDialog = new Dialog(context);
                ingreSelectDialog.setContentView(R.layout.custom_searchablespinner);
                ingreSelectDialog.getWindow().setLayout(750, 1000);
                ingreSelectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ingreSelectDialog.show();
                EditText editText = ingreSelectDialog.findViewById(R.id.edit_text);
                ListView listView = ingreSelectDialog.findViewById(R.id.list_view);
                Spinner unitSpinner = ingreSelectDialog.findViewById(R.id.unit_spinner);
                EditText editText1 = ingreSelectDialog.findViewById(R.id.edit_text1);
                Button addButton = ingreSelectDialog.findViewById(R.id.ingre_add_button);
                Button cancelButton = ingreSelectDialog.findViewById(R.id.cancel_button);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.ingredients));
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {//edittext????????????
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("position", "position = " + position);
                        editText.setText(adapter.getItem(position));
                        editText.setEnabled(false);
                        listView.setVisibility(View.INVISIBLE);
                    }
                });
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (existName.contains(editText.getText().toString())) {
                            rau.startToast("?????? ????????? ???????????????.");
                        } else {

                            View view = getLayoutInflater().inflate(R.layout.dynamic_ingre_item, null);
                            TextView name = view.findViewById(R.id.name);
                            TextView selectName = view.findViewById(R.id.writtenname);
                            TextView amount = view.findViewById(R.id.amount);
                            TextView editAmount = view.findViewById(R.id.writtenamount);
                            TextView unit = view.findViewById(R.id.unit);
                            ImageButton removeButton = view.findViewById(R.id.minus_button);
                            selectName.setText(editText.getText().toString());
                            editAmount.setText(editText1.getText().toString());
                            unit.setText(unitSpinner.getSelectedItem().toString());
                            removeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    existName.remove(selectName.getText().toString());
                                    linearlayout.removeView(view);
                                }
                            });
                            if (selectName.getText().toString().equals("") | editAmount.getText().toString().equals("")) {
                                rau.startToast("????????? ?????? ???????????????.");
                            } else {
                                linearlayout.addView(view);
                                existName.add(selectName.getText().toString());
                                ingreSelectDialog.dismiss();
                            }
                        }
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ingreSelectDialog.dismiss();
                    }
                });
            }
        });

        //????????? ?????? ????????? ?????? ?????? ??? ???????????????
        if (isEdit) {
            recipetitle.setText(recipePostF.getTitle());
            ArrayList<String> category_str = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.category)));
            recipecate.setSelection(category_str.indexOf(recipePostF.getCategory()));
            recipespicy.setSelection(recipePostF.getDegree_of_spicy());
            recipedescription.setText(recipePostF.getDescription());
            for (int i = 0; i < recipePostF.getRecipe_Ingredients().size(); i++) {
                View view = getLayoutInflater().inflate(R.layout.dynamic_ingre_item, null);
                TextView name = view.findViewById(R.id.name);
                TextView selectName = view.findViewById(R.id.writtenname);
                TextView amount = view.findViewById(R.id.amount);
                TextView editAmount = view.findViewById(R.id.writtenamount);
                TextView unit = view.findViewById(R.id.unit);
                ImageButton removeButton = view.findViewById(R.id.minus_button);
                selectName.setText(recipePostF.getRecipe_Ingredients().get(i).getName());
                editAmount.setText(Float.toString(recipePostF.getRecipe_Ingredients().get(i).getAmount()));
                unit.setText(recipePostF.getRecipe_Ingredients().get(i).getUnit());
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearlayout.removeView(view);
                    }
                });
                existName.add(recipePostF.getRecipe_Ingredients().get(i).getName());
                linearlayout.addView(view);
            }
        }
    }

    class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            // FAB Click ????????? ?????? ??????
            if (!isEdit) {
                //?????? ?????? ????????? ???????????? ?????? ??????
                if ((recipetitle.getText().toString().equals(""))
                        | (recipecate.getSelectedItem().toString().equals("????????? ??????"))
                        | (recipespicy.getSelectedItem().toString().equals("????????? ??????"))
                        | (recipedescription.getText().toString().equals(""))) {
                    rau.startToast("?????? ????????? ??????????????????.");
                } else {
                    custon_progressDialog.show();
                    newRecipe.setNickname(ControlLogin_f.userinfo.getNickname());
                    newRecipe.setTitle(recipetitle.getText().toString());
                    newRecipe.setCategory(recipecate.getSelectedItem().toString());
                    newRecipe.setDegree_of_spicy(Integer.parseInt(recipespicy.getSelectedItem().toString()));
                    newRecipe.setDescription(recipedescription.getText().toString());
                    for (int i = 0; i < linearlayout.getChildCount(); i++) {
                        View tempview = linearlayout.getChildAt(i);
                        TextView nameTemp = tempview.findViewById(R.id.writtenname);
                        TextView amountTemp = tempview.findViewById(R.id.writtenamount);
                        TextView unitTemp = tempview.findViewById(R.id.unit);

                        String temp_ingrename = nameTemp.getText().toString();
                        float temp_amount = Float.parseFloat(amountTemp.getText().toString());

                        Recipe_Ingredients tempingredients = new Recipe_Ingredients();
                        tempingredients.setName(temp_ingrename);
                        tempingredients.setAmount(temp_amount);
                        tempingredients.setUnit(unitTemp.getText().toString());
                        recipe_ingredients.add(tempingredients);
                        Log.d("????????? ?????? ??????", "i =" + i);
                    }
                    newRecipe.setRecipe_Ingredients(recipe_ingredients);

                    Log.d("recipe", newRecipe.toString());

                    threadFlag.set(true);
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (responseCode == 200) {
                                responseCode = -1;
                                threadFlag.set(false);
                                custon_progressDialog.dismiss();
                                Log.d("????????? ??????", "??????");
                                onBackPressed();
                            } else if (responseCode == 500) {
                                responseCode = -1;
                                threadFlag.set(false);
                                custon_progressDialog.dismiss();
                                rau.startDialog(0, "?????? ??????", "????????? ????????? ??????????????????.", new ArrayList<>(Arrays.asList("??????")));
                            } else if (responseCode == 502) {
                                responseCode = -1;
                                threadFlag.set(false);
                                custon_progressDialog.dismiss();
                                rau.startDialog(0, "?????? ??????", "??? ??? ?????? ???????????????.", new ArrayList<>(Arrays.asList("??????")));
                            }
                        }
                    };

                    class NewRunnable implements Runnable {
                        @Override
                        public void run() {
                            for (int i = 0; i < 30; i++) {
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (threadFlag.get())
                                    runOnUiThread(runnable);
                                else {
                                    i = 30;
                                }
                            }
                        }
                    }
                    ControlRecipe_f crf = new ControlRecipe_f();
                    crf.addRecipe(newRecipe);

                    NewRunnable nr = new NewRunnable();
                    Thread t = new Thread(nr);
                    t.start();
                }
            } else {
                //?????? ?????? ????????? ???????????? ?????? ??????
                if ((recipetitle.getText().toString().equals(""))
                        | (recipecate.getSelectedItem().toString().equals("????????? ??????"))
                        | (recipespicy.getSelectedItem().toString().equals("????????? ??????"))
                        | (recipedescription.getText().toString().equals(""))) {
                    rau.startToast("?????? ????????? ??????????????????.");
                } else {
                    custon_progressDialog.show();
                    int index = 0;
                    newRecipe.setNickname(ControlLogin_f.userinfo.getNickname());
                    newRecipe.setTitle(recipetitle.getText().toString());
                    newRecipe.setCategory(recipecate.getSelectedItem().toString());
                    newRecipe.setDegree_of_spicy(Integer.parseInt(recipespicy.getSelectedItem().toString()));
                    newRecipe.setDescription(recipedescription.getText().toString());

                    for (int i = index; i < linearlayout.getChildCount(); i++) {
                        View tempview = linearlayout.getChildAt(i);
                        TextView nameTemp = tempview.findViewById(R.id.writtenname);
                        TextView amountTemp = tempview.findViewById(R.id.writtenamount);
                        String temp_ingrename = nameTemp.getText().toString();
                        TextView unitTemp = tempview.findViewById(R.id.unit);
                        float temp_amount = Float.parseFloat(amountTemp.getText().toString());

                        Recipe_Ingredients tempingredients = new Recipe_Ingredients();
                        tempingredients.setId(0);
                        tempingredients.setName(temp_ingrename);
                        tempingredients.setPost_id(recipePostF.getPost_id());
                        tempingredients.setAmount(temp_amount);
                        tempingredients.setUnit(unitTemp.getText().toString());
                        recipe_ingredients.add(tempingredients);

                        Log.d("????????? ?????? ??????", "i =" + i);
                    }
                    newRecipe.setRecipe_Ingredients(recipe_ingredients);

                    Log.d("recipe", newRecipe.toString());

                    threadFlag.set(true);
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (responseCode == 200) {
                                responseCode = -1;
                                threadFlag.set(false);
                                custon_progressDialog.dismiss();
                                Log.d("????????? ??????", "??????");
                                onBackPressed();
                            } else if (responseCode == 500) {
                                responseCode = -1;
                                threadFlag.set(false);
                                custon_progressDialog.dismiss();
                                rau.startDialog(0, "????????? ??????", "????????? ????????? ??????????????????.", new ArrayList<>(Arrays.asList("??????")));
                            } else if (responseCode == 502) {
                                responseCode = -1;
                                threadFlag.set(false);
                                custon_progressDialog.dismiss();
                                rau.startDialog(0, "?????? ??????", "??? ??? ?????? ???????????????.", new ArrayList<>(Arrays.asList("??????")));
                            }
                        }
                    };

                    class NewRunnable implements Runnable {
                        @Override
                        public void run() {
                            for (int i = 0; i < 30; i++) {
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (threadFlag.get())
                                    runOnUiThread(runnable);
                                else {
                                    i = 30;
                                }
                            }
                        }
                    }
                    newRecipe.setPost_id(recipePostF.getPost_id());
                    ControlRecipe_f crf = new ControlRecipe_f();
                    crf.editRecipe(newRecipe);

                    NewRunnable nr = new NewRunnable();
                    Thread t = new Thread(nr);
                    t.start();
                }
            }
        }
    }

    //spinner hint??? ?????? ????????? ?????????
    public class SpinnerWithHintAdapter extends ArrayAdapter<String> {

        Context context;

        public SpinnerWithHintAdapter(Context context, int resource, String[] items) {
            super(context, resource, items);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView tvSpinner = (TextView) view;
            if (position == getCount()) {
                // setting the hint text color primary color
                tvSpinner.setTextColor(ContextCompat.getColor(context, com.google.android.libraries.places.R.color.quantum_grey
                ));
            } else {
                tvSpinner.setTextColor(Color.BLACK);
            }
            return view;
        }

        @Override
        public int getCount() {
            // don't display last item. It is used as hint.
            int count = super.getCount();
            return count > 0 ? count - 1 : count;
        }
    }

    class RecipeAdd_UI implements Control {
        @Override
        public void startToast(String message) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
            TextView toast_textview = layout.findViewById(R.id.toast_textview);
            toast_textview.setText(String.valueOf(message));
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, 0, 50); //TODO ???????????? ???????????? ???????????? (?????? ??????)
            toast.setDuration(Toast.LENGTH_SHORT); //????????? ?????? ??????
            toast.setView(layout);
            toast.show();
        }

        @Override
        public void startDialog(int type, String title, String message, List<String> btnTxtList) {
            Custom_Dialog cd = new Custom_Dialog(RecipeAddActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        @Override
        public void changePage(int dest) {
        }
    }

}
