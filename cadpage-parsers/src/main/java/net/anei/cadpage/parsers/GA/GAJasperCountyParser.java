package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class GAJasperCountyParser extends DispatchB2Parser {

  public GAJasperCountyParser() {
    super("JASPERCO911:", null, "JASPER COUNTY", "GA", B2_FORCE_CALL_CODE | B2_CROSS_FOLLOWS);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "ALCOVY SHORES",
        "BALL GROUND",
        "BEAR CREEK",
        "BECOMES POST",
        "BETHEL CHURCH",
        "BLUE HERON",
        "CONCORD CHURCH",
        "COUNTY LINE",
        "DAVIS FORD",
        "HENDERSON MILL",
        "HERRING GULL",
        "JACKSON LAKE",
        "LAKE SHORE",
        "LIBERTY CHURCH",
        "MCELHENEY STILES",
        "MIDWAY CHURCH",
        "MISS SARAH",
        "MOURNING DOVE",
        "PITTS CHAPEL E POST",
        "ROCK EAGLE",
        "SMITH BROCK",
        "STAG RUN",
        "WHIP POOR WILL WENDY HILL",
        "WILD TURKEY"
    );
    setupSpecialStreets("BEAR CREEK PT");
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("NAME", "INFO"); 
  }
  
  @Override
  protected boolean parseAddrField(String field, Data data) {
    field = field.replace('@',  '/');
    if (!super.parseAddrField(field, data)) return false;
    
    // They do not use the normal name/phone convention.  If we find a
    // parsed name, it really should be something else
    if (data.strName.length() > 0) {
      if (data.strName.startsWith("/")) {
        String saveCross = data.strCross;
        data.strCross = "";
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, data.strName.substring(1).trim(), data);
        if (saveCross.length() == 0) {
          data.strAddress = append(data.strAddress, " & ", data.strCross);
          data.strCross = "";
        } else {
          data.strCross = append(saveCross, " / ", data.strCross);
        }
        data.strSupp = getLeft();
      } else {
        data.strSupp = data.strName;
      }
      data.strName = "";
    }
    return true;
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT W/POSSIBLE INJURIES",
      "AUTO ACCIDENT/FATALITY",
      "BLEEDING,HEMORI,HEMORH",
      "CARDIAC ARREST / HEART ATTACK",
      "CHEST PAIN",
      "CVA/STROKE",
      "EXPLOSION",
      "FALL",
      "FIRE",
      "FIRE ALARM",
      "FIRE/OTHER",
      "FIRE / BRUSH",
      "FIRE / STRUCTURE",
      "FIRE/UNKNOWN",
      "FIRE/VEHICLE",
      "HIT AND RUN",
      "LACERATION",
      "LIFT ASSIST/ASSIST INVALID",
      "MISSING PERSON",
      "OVERDOSE",
      "PREGNANCY",
      "SHORT/BREATH/ASTHMA",
      "UNCONSCIOUS",
      "VEHICLE ACCIDENT/ENTRAPMENT",
      "VEHICLE ACC W/INJURIES"
  );
}
