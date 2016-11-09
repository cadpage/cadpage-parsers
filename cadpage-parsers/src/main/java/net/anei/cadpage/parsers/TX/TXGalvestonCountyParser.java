package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class TXGalvestonCountyParser extends DispatchOSSIParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile(".* FIRE", Pattern.CASE_INSENSITIVE);
  
  public TXGalvestonCountyParser() {
    super(CITY_CODES, "GALVESTON COUNTY", "TX",
          "ADDR/aS8CI! INFO+");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "ALGOA FRNDSWD",
        "ANDERSON WAYS",
        "B BAR",
        "BLIMP BASE",
        "BOB SMITH",
        "CAPTAIN BLIGH",
        "CAPTAIN HOOK",
        "COUNTRY MEADOW",
        "COVE VIEW",
        "EDWARD TEACH",
        "FRANCIS DRAKE",
        "HALF MOON",
        "HENRY MORGAN",
        "IKE FRANK",
        "JAMAICA BEACH",
        "JAMAICA COVE",
        "JAMAICA INN",
        "JEAN LAFITTE",
        "JOHN SILVER",
        "JOLLY ROGER",
        "LEWIS SCOTT",
        "LONE PINE",
        "MOBY DICK",
        "MORNING GLORY",
        "PONCE DE LEON",
        "SAN LOUIS PASS",
        "SAN LUIS PASS",
        "SNOWY EGRET",
        "SPANISH MAIN",
        "TWIN CREEK"
    );
  }
  
  @Override
  public String getFilter() {
    return "iammessaging.com,777,888,410,CAD@ci.dickinson.tx.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      if (SUBJECT_PTN.matcher(subject).matches()) {;
        data.strSource = subject;
        break;
      }
      
      if (isPositiveId()) break;
      
      return false;
    } while (false);
    
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private static final Pattern BOX_ADDR_PTN = Pattern.compile("(\\d+) +(.*)");
  private static final Pattern NUMBER_HWY_PTN = Pattern.compile("\\b(\\d+)(US|FT|TX|FM)\\b");
  private static final Pattern HWY_ST_PTN = Pattern.compile("\\b(HWY \\d+) (ST|RD)\\b");
  private static final Pattern AVE_X_ST_PTN = Pattern.compile("\\bAVE [A-Z](?: HALF)?(?: REAR)?(?= ST\\b)");
  private static final Pattern AVE_X_ST_PTN2 = Pattern.compile("\\b(AVE_[A-Z](?:_HALF)?(?:_REAR)?) ST\\b");
  private static final Pattern NUMBER_DASH_PTN = Pattern.compile("^(\\d+)-(?![A-Z] |BLK )");
  private static final Pattern NUMBER_HALF_PTN = Pattern.compile("\\b(\\d+)-HALF\\b");
  private static final Pattern NUMBER_HALF_PTN2 = Pattern.compile("\\b(\\d+)\\.5\\b");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("FYI:")) field = field.substring(4).trim();
      else if (field.startsWith("Update:")) field = field.substring(7).trim();
      
      Matcher match = BOX_ADDR_PTN.matcher(field);
      if (match.matches()) {
        data.strBox = match.group(1);
        field = match.group(2);
      }

      int saveLen = field.length();
      field = NUMBER_HALF_PTN.matcher(field).replaceAll("$1.5");
      boolean repHalf = field.length() != saveLen;

      field = NUMBER_HWY_PTN.matcher(field).replaceAll("$1 $2");
      match = AVE_X_ST_PTN.matcher(field);
      if (match.find()) {
        field = field.substring(0,match.start()) + match.group().replace(' ', '_') + field.substring(match.end());
      }
      
      field = HWY_ST_PTN.matcher(field).replaceAll("$1");
      
      super.parse(field, data);
      
      match = AVE_X_ST_PTN2.matcher(data.strAddress);
      if (match.find()) {
        data.strAddress = data.strAddress.substring(0,match.start()) + match.group(1).replace('_', ' ').replace("HALF", "1/2") + data.strAddress.substring(match.end());
      }
      
      data.strAddress = NUMBER_DASH_PTN.matcher(data.strAddress).replaceFirst("$1 ");
      if (repHalf) data.strAddress = NUMBER_HALF_PTN2.matcher(data.strAddress).replaceAll("$1-HALF");
      if (data.strAddress.length() == 0) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "BOX " + super.getFieldNames() + " X";
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return NUMBER_HALF_PTN.matcher(addr).replaceAll("$1");
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ANIMAL ASTRAY",
      "ASSAULT",
      "ASSIST EMS",
      "ASSIST LAW ENFORCEMENT",
      "ASSIST OTHER AGENCY",
      "AUTO PEDESTRIAN ACCIDENT",
      "BOMB THREAT",
      "BONFIRE",
      "BURNS",
      "CANCEL",
      "CHEST PAINS",
      "CHOKE",
      "DEAD ON SCENE",
      "DEAD ON SCENE - DICKINSON",
      "DIABETIC SICK CALL",
      "DIFFICULTY BREATHING",
      "DROWNING",
      "DUP",
      "DUP FM",
      "FALLEN SUBJECT",
      "FIRE ALARM",
      "FIRE - NON SPECIFIC",
      "FIRST RESPONDER WITH EMS",
      "GAS LEAK",
      "GRASS FIRE",
      "HAZMAT",
      "INDUSTRIAL ACCIDENT",
      "INJURED PERSON",
      "INJURY TO A CHILD",
      "LABOR",
      "LANDING ZONE",
      "LINE DOWN",
      "MAJOR ACCIDENT",
      "MARINE RELATED",
      "MEDICAL ALARM",
      "MEDICAL ASSIST",
      "MUTUAL AID",
      "NON EMERGENCY TRANSFER",
      "OVERDOSE",
      "POLE FIRE",
      "RESCUE",
      "SEIZURE",
      "SICK CALL",
      "SMOKE INVESTIGATION",
      "STROKE",
      "STRUCTURE FIRE",
      "SUICIDE ATTEMPTED OR COMPLETED",
      "TEST CALL ONLY",
      "TRAFFIC STOP",
      "TRASH FIRE",
      "UNCONSCIOUS PERSON",
      "UNKNOWN REQUIRES EMS",
      "VEHICLE FIRE",
      "WASH DOWN"
  );
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "SANL SLFD",      "SAN LEON"
  });
}
