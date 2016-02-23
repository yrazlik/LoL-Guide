package com.yrazlik.loltr.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.yrazlik.loltr.R;

import java.util.ArrayList;

/**
 * Created by yrazlik on 8/6/15.
 */
public class FilterActivity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener{

    private final String TAG_HEALTH = "Health";
    private final String TAG_MAGIC_RESISTANCE = "SpellBlock";
    private final String TAG_HEALTH_REGEN = "HealthRegen";
    private final String TAG_ARMOR = "Armor";
    private final String TAG_DAMAGE = "Damage";
    private final String TAG_CRITICAL = "CriticalStrike";
    private final String TAG_ATTACK_SPEED = "AttackSpeed";
    private final String TAG_LIE_STEAL = "LifeSteal";
    private final String TAG_ARMOR_PENET = "ArmorPenetration";
    private final String TAG_SPELL_DAMAGE = "SpellDamage";
    private final String TAG_COOLDOWN_REDUCTION = "CooldownReduction";
    private final String TAG_MANA = "MANA";
    private final String TAG_MANA_REG = "ManaRegen";
    private final String TAG_MAGIC_PENET = "MagicPenetration";
    private final String TAG_SPELL_VAMP = "SpellVamp";
    private final String TAG_VISION = "Vision";
    private final String TAG_MOVEMENT = "NonBootsMovement";
    private final String TAG_BOOTS = "Boots";
    private final String TAG_GOLD = "GoldPer";

    private final String EXTRA_FILTER = "EXTRA_FILTER";



    private CheckBox cbHealth, cbMagicResistance, cbHealthRegen, cbArmor, cbDamage, cbCriticalStrike,
            cbAttackSpeed, cbLifeSteal, cbArmorPenetration, cbSpellDamage, cbCooldownReduction, cbMana,
            cbManaReg, cbMagicPenetration, cbSpellVamp, cbVision, cbMovement, cbConsumable, cbGold;

    private Button buttonOk, buttonCancel;

    private ArrayList<String> filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_filter_items);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + getString(R.string.filter) + "</font>"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D9B40617")));
        getSupportActionBar().show();
        Intent i = getIntent();
        if(i != null){
             filters = i.getStringArrayListExtra(EXTRA_FILTER);
            if(filters == null){
                filters = new ArrayList<String>();
            }
        }
        initUI();
    }

    private void initUI(){
        cbHealth = (CheckBox) findViewById(R.id.cbHealth);
        cbMagicResistance = (CheckBox) findViewById(R.id.cbMagicResistance);
        cbHealthRegen = (CheckBox) findViewById(R.id.cbHealthRegen);
        cbArmor = (CheckBox) findViewById(R.id.cbArmor);
        cbDamage = (CheckBox) findViewById(R.id.cbDamage);
        cbCriticalStrike = (CheckBox) findViewById(R.id.cbCriticalStrike);
        cbAttackSpeed = (CheckBox) findViewById(R.id.cbAttackSpeed);
        cbLifeSteal = (CheckBox) findViewById(R.id.cbLifeSteal);
        cbArmorPenetration = (CheckBox) findViewById(R.id.cbArmorPenetration);
        cbSpellDamage = (CheckBox) findViewById(R.id.cbSpellDamage);
        cbCooldownReduction = (CheckBox) findViewById(R.id.cbCooldownReduction);
        cbMana = (CheckBox) findViewById(R.id.cbMana);
        cbManaReg = (CheckBox) findViewById(R.id.cbManaReg);
        cbMagicPenetration = (CheckBox) findViewById(R.id.cbMagicPenetration);
        cbSpellVamp = (CheckBox) findViewById(R.id.cbSpellVamp);
        cbVision = (CheckBox) findViewById(R.id.cbVision);
        cbMovement = (CheckBox) findViewById(R.id.cbMovement);
        cbConsumable = (CheckBox) findViewById(R.id.cbConsumable);
        cbGold = (CheckBox) findViewById(R.id.cbGold);
        buttonOk = (Button) findViewById(R.id.buttonOk);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);

        cbHealth.setOnCheckedChangeListener(this);
        cbMagicResistance.setOnCheckedChangeListener(this);
        cbHealthRegen.setOnCheckedChangeListener(this);
        cbArmor.setOnCheckedChangeListener(this);
        cbDamage.setOnCheckedChangeListener(this);
        cbCriticalStrike.setOnCheckedChangeListener(this);
        cbAttackSpeed.setOnCheckedChangeListener(this);
        cbLifeSteal.setOnCheckedChangeListener(this);
        cbArmorPenetration.setOnCheckedChangeListener(this);
        cbSpellDamage.setOnCheckedChangeListener(this);
        cbCooldownReduction.setOnCheckedChangeListener(this);
        cbMana.setOnCheckedChangeListener(this);
        cbManaReg.setOnCheckedChangeListener(this);
        cbMagicPenetration.setOnCheckedChangeListener(this);
        cbSpellVamp.setOnCheckedChangeListener(this);
        cbVision.setOnCheckedChangeListener(this);
        cbMovement.setOnCheckedChangeListener(this);
        cbConsumable.setOnCheckedChangeListener(this);
        cbGold.setOnCheckedChangeListener(this);

        if(filters != null && filters.size() > 0){
            for(String filter : filters){
                if(filter.equalsIgnoreCase(TAG_HEALTH)){
                    cbHealth.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_MAGIC_RESISTANCE)){
                    cbMagicResistance.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_HEALTH_REGEN)){
                    cbHealthRegen.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_ARMOR)){
                    cbArmor.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_DAMAGE)){
                    cbDamage.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_CRITICAL)){
                    cbCriticalStrike.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_ATTACK_SPEED)){
                    cbAttackSpeed.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_LIE_STEAL)){
                    cbLifeSteal.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_ARMOR_PENET)){
                    cbArmorPenetration.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_SPELL_DAMAGE)){
                    cbSpellDamage.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_COOLDOWN_REDUCTION)){
                    cbCooldownReduction.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_MANA)){
                    cbMana.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_MANA_REG)){
                    cbManaReg.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_MAGIC_PENET)){
                    cbMagicPenetration.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_SPELL_VAMP)){
                    cbSpellVamp.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_VISION)){
                    cbVision.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_HEALTH)){
                    cbHealth.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_MOVEMENT) || filter.equalsIgnoreCase(TAG_BOOTS)){
                    cbMovement.setChecked(true);
                }

                if(filter.equalsIgnoreCase(TAG_GOLD)){
                    cbGold.setChecked(true);
                }
            }
        }


        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(EXTRA_FILTER, filters);
                setResult(RESULT_OK, returnIntent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.slide_bottom_out);
            }
        });



        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.slide_bottom_out);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cbHealth:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_HEALTH)){
                        filters.add(TAG_HEALTH);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_HEALTH)){
                        filters.remove(TAG_HEALTH);
                    }
                }
                break;
            case R.id.cbMagicResistance:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_MAGIC_RESISTANCE)){
                        filters.add(TAG_MAGIC_RESISTANCE);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_MAGIC_RESISTANCE)){
                        filters.remove(TAG_MAGIC_RESISTANCE);
                    }
                }
                break;
            case R.id.cbHealthRegen:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_HEALTH_REGEN)){
                        filters.add(TAG_HEALTH_REGEN);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_HEALTH_REGEN)){
                        filters.remove(TAG_HEALTH_REGEN);
                    }
                }
                break;
            case R.id.cbArmor:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_ARMOR)){
                        filters.add(TAG_ARMOR);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_ARMOR)){
                        filters.remove(TAG_ARMOR);
                    }
                }
                break;
            case R.id.cbDamage:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_DAMAGE)){
                        filters.add(TAG_DAMAGE);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_DAMAGE)){
                        filters.remove(TAG_DAMAGE);
                    }
                }
                break;
            case R.id.cbCriticalStrike:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_CRITICAL)){
                        filters.add(TAG_CRITICAL);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_CRITICAL)){
                        filters.remove(TAG_CRITICAL);
                    }
                }
                break;
            case R.id.cbAttackSpeed:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_ATTACK_SPEED)){
                        filters.add(TAG_ATTACK_SPEED);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_ATTACK_SPEED)){
                        filters.remove(TAG_ATTACK_SPEED);
                    }
                }
                break;
            case R.id.cbLifeSteal:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_LIE_STEAL)){
                        filters.add(TAG_LIE_STEAL);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_LIE_STEAL)){
                        filters.remove(TAG_LIE_STEAL);
                    }
                }
                break;
            case R.id.cbArmorPenetration:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_ARMOR_PENET)){
                        filters.add(TAG_ARMOR_PENET);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_ARMOR_PENET)){
                        filters.remove(TAG_ARMOR_PENET);
                    }
                }
                break;
            case R.id.cbSpellDamage:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_SPELL_DAMAGE)){
                        filters.add(TAG_SPELL_DAMAGE);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_SPELL_DAMAGE)){
                        filters.remove(TAG_SPELL_DAMAGE);
                    }
                }
                break;
            case R.id.cbCooldownReduction:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_COOLDOWN_REDUCTION)){
                        filters.add(TAG_COOLDOWN_REDUCTION);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_COOLDOWN_REDUCTION)){
                        filters.remove(TAG_COOLDOWN_REDUCTION);
                    }
                }
                break;
            case R.id.cbMana:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_MANA)){
                        filters.add(TAG_MANA);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_MANA)){
                        filters.remove(TAG_MANA);
                    }
                }
                break;
            case R.id.cbManaReg:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_MANA_REG)){
                        filters.add(TAG_MANA_REG);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_MANA_REG)){
                        filters.remove(TAG_MANA_REG);
                    }
                }
                break;
            case R.id.cbMagicPenetration:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_MAGIC_PENET)){
                        filters.add(TAG_MAGIC_PENET);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_MAGIC_PENET)){
                        filters.remove(TAG_MAGIC_PENET);
                    }
                }
                break;
            case R.id.cbSpellVamp:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_SPELL_VAMP)){
                        filters.add(TAG_SPELL_VAMP);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_SPELL_VAMP)){
                        filters.remove(TAG_SPELL_VAMP);
                    }
                }
                break;
            case R.id.cbVision:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_VISION)){
                        filters.add(TAG_VISION);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_VISION)){
                        filters.remove(TAG_VISION);
                    }
                }
                break;
            case R.id.cbMovement:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_MOVEMENT)){
                        filters.add(TAG_MOVEMENT);
                    }
                    if(filters != null && !filters.contains(TAG_BOOTS)){
                        filters.add(TAG_BOOTS);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_MOVEMENT)){
                        filters.remove(TAG_MOVEMENT);
                    }
                    if(filters != null && filters.contains(TAG_BOOTS)){
                        filters.remove(TAG_BOOTS);
                    }
                }
                break;
            case R.id.cbGold:
                if(isChecked){
                    if(filters != null && !filters.contains(TAG_GOLD)){
                        filters.add(TAG_GOLD);
                    }
                }else {
                    if(filters != null && filters.contains(TAG_GOLD)){
                        filters.remove(TAG_GOLD);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.slide_top_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.slide_bottom_out);
    }
}
