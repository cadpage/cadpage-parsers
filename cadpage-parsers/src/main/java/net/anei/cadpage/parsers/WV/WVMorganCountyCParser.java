package net.anei.cadpage.parsers.WV;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchX01Parser;

public class WVMorganCountyCParser extends DispatchX01Parser {

  public WVMorganCountyCParser() {
    super(CITY_CODES, "MORGAN COUNTY", "WV");
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    body = stripFieldStart(body, "fromcad\nCAD\n");
    body = body.replace("&<", "<").replace("&", "&amp;");
    if (!super.parseHtmlMsg(subject, body, data)) return false;

    int pt = data.strCity.indexOf('/');
    if (pt >= 0) {
      data.strState = data.strCity.substring(pt+1);
      data.strCity = data.strCity.substring(0,pt);
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  private static final Properties CITY_CODES =  buildCodeTable(new String[] {
      "AM",  "AMARANTH/PA",
      "AU",  "AUGUSTA/WV",
      "BA",  "BARTON/MD",
      "BC",  "BIG COVE TANNER/PA",
      "BF",  "BELFAST/PA",
      "BH",  "BUNKER HILL/WV",
      "BL",  "BLOOMERY/WV",
      "BO",  "BOONSBORO/MD",
      "BP",  "BIG POOL/MD",
      "BR",  "BRUCETOWN/VA",
      "BS",  "BERKELEY SPRINGS/WV",
      "BV",  "BUCK VALLEY/PA",
      "BW",  "BREEZEWOOD/PA",
      "CB",  "CAPON BRIDGE/WV",
      "CCO", "CLARKE COUNTY/VA",
      "CJ",  "CROSS JUNCTION/VA",
      "CL",  "CLEARBROOK/VA",
      "CO",  "CORRIGANVILLE/MD",
      "CP",  "CAPON SPRINGS/WV",
      "CS",  "CLEAR SPRING/MD",
      "CU",  "CUMBERLAND/MD",
      "CV",  "CLEARVILLE/PA",
      "DR",  "DELRAY/WV",
      "DT",  "DOTT/PA",
      "EL",  "ELLERSLIE/MD",
      "EV",  "EVERETT/PA",
      "FB",  "FROSTBURG/MD",
      "FL",  "FLINTSTONE/MD",
      "FP",  "FAIRPLAY/MD",
      "FT",  "FUNKSTOWN/MD",
      "FW",  "FALLING WATERS/WV",
      "GC",  "GREAT CACAPON/WV",
      "GE",  "GERRARDSTOWN/WV",
      "GO",  "GORE/VA",
      "GS",  "GREEN SPRING/WV",
      "HC",  "HANCOCK/MD",
      "HC1", "HIGHFIELD-CASCADE/MD",
      "HT",  "HUSTONTOWN/PA",
      "HT1", "HAGERSTOWN/MD",
      "HT2", "HAGERSTOWN/MD",
      "HT3", "HAGERSTOWN/MD",
      "HV",  "HIGH VIEW/WV",
      "HV1", "HEDGESVILLE/WV",
      "HY",  "HYNDMAN/PA",
      "IW",  "INWOOD/WV",
      "JC",  "JUNCTION/WV",
      "KI",  "KIRBY/WV",
      "KV",  "KEEDYSVILLE/MD",
      "LC",  "LONACONING/MD",
      "LH",  "LEHEW/WV",
      "LI",  "LICKING CREEK/PA",
      "LO",  "LITTLE ORLEANS/MD",
      "LU",  "LUKE/MD",
      "LV",  "LEVELS/WV",
      "MB",  "MARTINSBURG/WV",
      "MB1", "MARTINSBURG/WV",
      "MB2", "MARTINSBURG/WV",
      "MB3", "MARTINSBURG/WV",
      "MC",  "MCCONNELLSBURG/PA",
      "MCO", "MORGAN COUNTY/WV",
      "MD",  "MIDDLETOWN/VA",
      "MG",  "MAUGANSVILLE/MD",
      "MI",  "MIDLAND/MD",
      "MKT", "NEW MARKET/MD",
      "ML",  "MIDLOTHIAN/MD",
      "MS",  "MOUNT SAVAGE/MD",
      "NM",  "NEEDMORE/PA",
      "OT",  "OLDTOWN/MD",
      "PAW", "PAW PAW/WV",
      "PO",  "POINTS/WV",
      "PP",  "PAW PAW/WV",
      "PV",  "PURGITSVILLE/WV",
      "RA",  "RAWLINGS/MD",
      "RI",  "RIO/WV",
      "RM",  "ROMNEY/WV",
      "RV",  "ROHRERSVILLE/MD",
      "SB",  "SHARPSBURG/MD",
      "SC",  "STEPHENS CITY/VA",
      "SH",  "SHANKS/WV",
      "SI",  "SIPES MILL/PA",
      "SJ",  "ST JAMES/MD",
      "SM",  "SMITHSBURG/MD",
      "SN",  "STEPHENSON/MD",
      "SP",  "SPRINGFIELD/WV",
      "ST",  "STAR TANNERY/VA",
      "STT", "ST THOMAS/PA",
      "SV",  "SLANESVILLE/WV",
      "TB",  "BERKELEY SPRINGS/WV",
      "TOB", "BERKELEY SPRINGS/WV",
      "TH1", "THREE CHURCHES/WV",
      "WAS", "WASHINGTON COUNTY/MD",
      "WE",  "WESTERNPORT/MD",
      "WF",  "WARFORDSBURG/PA",
      "WI",  "WINCHESTER/VA",
      "WI1", "WINCHESTER/VA",
      "WP",  "WILLIAMSPORT/MD",
      "YS",  "YELLOW SPRING/WV"
  });
}
