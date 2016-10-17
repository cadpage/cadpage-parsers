package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;

/**
 * Gaston County, NC
 */

public class NCGastonCountyBParser extends DispatchA9Parser {
  
  private static final Pattern CODE_PTN = Pattern.compile("\\d{1,2}[A-Z][A-Z0-9]*");
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "COUNTY",    ""
  });
  
  public NCGastonCountyBParser() {
    super(CITY_CODES, "GASTON COUNTY", "NC");
  }
  
  @Override
  public String getFilter() {
    return "NWSAdmin@cityofgastonia.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (CODE_PTN.matcher(data.strCall).matches()) {
      data.strCode = data.strCall;
      String call = CODE_TABLE.getCodeDescription(data.strCode);
      if (call == null) call = "MEDICAL";
      data.strCall = call;
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }
  
  private static final CodeTable CODE_TABLE = new CodeTable(
      "1D",    "Abdominal Pain Not alert",
      "2D",    "Allergic reactions/Hives/Medical reactions",
      "3D",    "Animal Bite",
      "4D",    "Assault / Sexual Assault",
      "5D",    "Back Pain (Non-Traumatic or Recent)",
      "6D",    "Breathing Problems",
      "7C",    "Burns / Explosion",
      "7D",    "Burns / Explosion Multi Victims",
   // 8C
      "8D",    "Carbon Monoxide/Inhalation/Hazmat",
      "9B1G",  "Cardiac Arrest / Death Submersion (>6 hours)",
      "9E",    "Cardiac Arrest / Death",
      "10D",   "Chest Pain",
      "11D",   "Choking",
      "12D",   "Convulsions / Seizures",
      "13D",   "Diabetic Problems",
      "14D",   "Drowning (Near) / Diving Accident",
      "15D",   "Electrocution / Lightning",
      "16D",   "Eye Problems / Injuries",
      "17D",   "Falls / Back Injuries (Traumatic)",
      "19D",   "Heart Problems",
      "20D2H", "Heat Exposure",
      "20D2C", "Cold Exposure",
      "21D",   "Hemorrhage / Lacerations",
      "22D",   "Trench Collapse",
      "23D",   "Overdose / Poisoning (Ingestion)",
      "24D",   "Pregnancy / Childbirth/ Miscarriage",
      "25D",   "Psychiatric / Suicide Attempt",
      "26D",   "Sick Person (Specific Diagnosis)",
      "27D",   "Stab / Gunshot / Penetrating Trauma",
      "28C",   "Stroke (CVA)",
   // 29A1
      "29B",   "MVA possible injuries",
      "29D",   "MVA with rollover, pin-in, entrapment, ejection, fatality, etc..",
   // 30B
      "30D",   "Traumatic Injury (Specific)",
      "31D",   "Unconscious / Fainting (Near)",
      "32D",   "Unknown Problem (Man Down)"
  ); 
}
