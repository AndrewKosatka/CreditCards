package it.myke.creditcards.disk;

import it.myke.creditcards.utils.Colors;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;

public class Lang {

    // It's a list of all the messages that can be sent to the player.
    public static String
            already_in_creation_process,
            atm_help_message,
            atm_not_valid,
            atm_placed,
            atm_received,
            atm_removed,
            banknote_doesnt_exists,
            banknote_gui_lore,
            banknote_gui_name_color,
            banknote_gui_value,
            banknote_received,
            banknotes_help_message,
            banknotes_unknown_subcommand,
            cant_card_yourself,
            card_created_successfully,
            card_creating,
            card_info_message,
            card_info_nocard,
            card_info_nocards_exists,
            card_not_valid,
            cardcreation_process_failed,
            cardtype_cost,
            cardtype_doesnt_exists,
            cardtype_name_color,
            confirm_card_message,
            confirm_card_message_hover,
            creditcard_command_distance,
            creditcard_create_command_usage,
            creditcard_help_message,
            creditcard_unknown_subcommand,
            deposit_limit_reached,
            deposit_max,
            insert_pin,
            inventory_full,
            max_withdraw_reached,
            money_deposited,
            money_withdrawn,
            no_perms,
            not_enough_money,
            not_enough_money_cardtype,
            only_players,
            payments_max,
            pin_not_correct,
            pinbook_failed_sending_viamessage,
            player_doesnt_exists,
            time_over_cardcreation_process,
            withdraw_cardtype_cost,
            withdraw_max,
            you_dont_have_a_card,
            max_credit_reached,
            credit_max,
            unlimited;




    /**
     * It loops through all the fields in the class, and if the field is a String, it sets the field's value to the value
     * of the field's name in the config
     *
     * @param langConfig The FileConfiguration object of the language file.
     */
    public void init(FileConfiguration langConfig) {

        for(Field field : getClass().getDeclaredFields()) {
            if(field.getType() == String.class) {
                field.setAccessible(true);
                try {
                    String value = Colors.colorParser(langConfig.getString(field.getName()));
                    field.set(this, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    }



}
