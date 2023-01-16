package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCDareCountyBParser extends FieldProgramParser {

  public NCDareCountyBParser() {
    super(CITY_CODES, "DARE COUNTY", "NC",
          "CAD_ID:ID! Call_Type:SKIP! Nature:CALL! Address:ADDR! City:CITY? Primary_Unit:UNIT? Call_Opened:TIMEDATE! " +
            "Lat:GPS1/d! Long:GPS2/d! ( ResponsePlanNumber:PLAN! NumberOfPlans:PLAN_CNT! | ) Call_Taker_Comments:INFO INFO/N+ END");
  }

  private static final Pattern LEAD_HTML_TAG_PTN = Pattern.compile("<([_A-Za-z]+)>");
  private static final Pattern TRAIL_HTML_TAG_PTN = Pattern.compile("</[_A-Za-z]+>");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = LEAD_HTML_TAG_PTN.matcher(body).replaceAll(" $1:");
    body = TRAIL_HTML_TAG_PTN.matcher(body).replaceAll(" ");
    String[] flds = body.split("\n");
    if (flds.length > 3) {
      return parseFields(flds, data);
    } else {
      body = body.replace("Call Taker Comments:", " Call Taker Comments:");
      return super.parseMsg(body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIMEDATE")) return new TimeDateField("\\d\\d:\\d\\d:\\d\\d +\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("PLAN")) return new MyPlanField();
    if (name.equals("PLAN_CNT")) return new MyPlanCntField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(';');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private class MyPlanField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      data.strSupp = append(data.strSupp, "\n", "Response Plan: " + field);
    }
  }

  private class MyPlanCntField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty() || !data.strSupp.contains("Response Plan:")) return;
      data.strSupp = data.strSupp + '/' + field;
    }
  }

  private static final Pattern INFO_DELIM_PTN = Pattern.compile(" *\\b\\d\\d:\\d\\d:\\d\\d +\\d\\d/\\d\\d/\\d{4} - [A-Z]+(?:, [A-Z])?\\b *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for ( String line : INFO_DELIM_PTN.split(field)) {
        if (line.startsWith("Unit:")) {
          String unit = line.substring(5).trim();
          if (!data.strUnit.contains(unit)) data.strUnit = append(data.strUnit, ",", unit);
        } else {
          data.strSupp = append(data.strSupp, "\n", line);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "INFO UNIT";
    }
  }

  static final Properties CITY_CODES = buildCodeTable(new String[] {
      "AVN",     "AVON",
      "BUX",     "BUXTON",
      "CLB",     "COLUMBIA",
      "COL",     "COLINGTON",
      "CRE",     "CRESWELL",
      "DCK",     "DUCK",
      "EDEN",    "EDENTON",
      "ELK",     "EAST LAKE",
      "ENG",     "ENGLEHARD",
      "FFD",     "FARFIELD",
      "FRI",     "FRISCO",
      "HAT",     "HATTERAS",
      "JARV",    "JARVISBURG",
      "KDH",     "KILL DEVIL HILLS",
      "KH",      "KITTY HAWK",
      "KHP",     "KITTY HAWK",
      "MAN",     "MANTEO",
      "MNH",     "MANNS HARBOR",
      "MPT",     "MARTINS POINT",
      "NGH",     "NAGS HEAD",
      "OCK",     "OCRACOKE",
      "PAN",     "PANTEGO",
      "PLY",     "PLYMOUTH",
      "PON",     "PONZER",
      "RI",      "ROANOKE ISLAND",
      "ROD",     "RODANTHE",
      "SAL",     "SALVO",
      "SCR",     "SCRANTON",
      "SSH",     "SOUTHERN SHORES",
      "STP",     "STUMPY POINT",
      "SWQ",     "SWANQUARTER",
      "WAN",     "WANCHESE",
      "WAV",     "WAVES"
  });
}
