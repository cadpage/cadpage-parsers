package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


public class DispatchPrintrakParser extends FieldProgramParser {
  
  /**
   * Flag indicating we should use the CMT1 field to replace the normal call field
   */
  public static final int FLG_USE_CMT1_CALL = 0x10;
  
  /**
   * Format versions
   */
  private static final int FLG_VERSION_MASK = 0xF;
  public static final int FLG_VERSION_1 = 0x0;
  public static final int FLG_VERSION_2 = 0x1;
  
  public DispatchPrintrakParser(String defCity, String defState) {
    this((String[])null, defCity, defState, null, 0);
  }
  
  public DispatchPrintrakParser(String defCity, String defState, String expTerm) {
    this((String[])null, defCity, defState, expTerm, 0);
  }
  
  public DispatchPrintrakParser(String defCity, String defState, int flags) {
    this((String[])null, defCity, defState, null, flags);
  }
  
  public DispatchPrintrakParser(Properties cityCodes, String defCity, String defState) {
    this(cityCodes, defCity, defState, null, 0);
  }
  
  public DispatchPrintrakParser(String[] cityList, String defCity, String defState) {
    this(cityList, defCity, defState, null, 0);
  }
  
  public DispatchPrintrakParser(Properties cityCodes, String defCity, String defState, String expTerm) {
    this(cityCodes, defCity, defState, expTerm, 0);
  }
  
  public DispatchPrintrakParser(String[] cityList, String defCity, String defState, String expTerm) {
    this(cityList, defCity, defState, expTerm, 0);
  }
  
  public DispatchPrintrakParser(Properties cityCodes, String defCity, String defState, int flags) {
    this(cityCodes, defCity, defState, null, flags);
  }
  
  public DispatchPrintrakParser(String[] cityList, String defCity, String defState, int flags) {
    this(cityList, defCity, defState, null, flags);
  }
  
  public DispatchPrintrakParser(Properties cityCodes, String defCity, String defState, String expTerm, int flags) {
    super(cityCodes, defCity, defState, getProgramStr(expTerm, flags));
  }
  
  public DispatchPrintrakParser(String[] cityList, String defCity, String defState, String expTerm, int flags) {
    super(cityList, defCity, defState, getProgramStr(expTerm, flags));
  }
  
  private static String getProgramStr(String expTerm, int flags) {
    String cmt1Fld = ((flags & FLG_USE_CMT1_CALL) != 0 ? "CALL2" : "INFO");

    int version = (flags & FLG_VERSION_MASK);
    String program = 
        (version == FLG_VERSION_1 ?
            "( TIME:TIME_INFO! TYP:CALL " +
            "| AD:ADDR! LOC:CITY TIME:TIME_INFO! TYP:CALL " +
            "| LOC:ADDR! AD:PLACE! DESC:CALL! BLD:APT! FLR:APT/D? APT:APT/D! TYP:CODE! MODCIR:CALL/SDS! CMT1:INFO/N+ " +
            "| PRI:PRI INC:ID CODE:CODE TYP:CALL! BLD:APT APT:APT AD:ADDR! APT:APT ( CTY:CITY | CITY:CITY ) MAP:MAP LOC:PLACE CALLER:NAME XST:X CN:NAME CMT1:" + cmt1Fld +  
              " Original_Location:PLACE2? CMT2:INFO/N CMT3:INFO/N CMT4:INFO/N CMT5:INFO/N Original_Location:PLACE2? CE:INFO? CMT2:INFO CALLER_STATEMENT:INFO? STATEMENT:INFO? TIME:TIME UNTS:UNIT XST:X XST2:X UNTS:UNIT XST:X XST2:X )"
        : version == FLG_VERSION_2 ?
            "TYP:CALL! LOC:PLACE! AD:ADDR/S! XST:X! CMT1:INFO! UNTS:UNIT!"
        : null);    
    return setExpectFlag(program, expTerm);
  }

  private static final Pattern GEN_ALERT_PTN = Pattern.compile("(?:([-A-Z0-9]+) +)?TIME: *(\\d\\d:\\d\\d)\\b *(.*)", Pattern.DOTALL);
  private static final Pattern BREAK_PTN = Pattern.compile(" *[\n\t]+ *");
  private static final Pattern SRC_PTN = Pattern.compile("([^:]+?) +([A-Z0-9]+:.*)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = GEN_ALERT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("SRC TIME INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSource = getOptGroup(match.group(1));
      data.strTime = match.group(2);
      data.strSupp = match.group(3).trim();
      return true;
    }
    
    body = BREAK_PTN.matcher(body).replaceAll(" ");
    match = SRC_PTN.matcher(body);
    if (match.matches()) {
      data.strSource = match.group(1);
      body = match.group(2);
    }
    body = body.replace(" CMTS:", " CMT1:").replace("CMT:",  " CMT1:").replace("AD:", " AD:").replace("UNITS:", " UNTS:").replace(" X-ST:", " XST:");
    body = body.replace(" CALLER / STATEMENT:", " CALLER STATEMENT:");
    body = body.replace(" CALLER CMT2:", " CMT2:");
    return super.parseMsg(body.trim(), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new BasePriorityField();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("APT")) return new BaseAptField();
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("TIME")) return new BaseTimeField();
    if (name.equals("TIME_INFO")) return new BaseTimeInfoField();
    if (name.equals("CALL2")) return new BaseCall2Field();
    if (name.equals("PLACE2")) return new BasePlace2Field();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("X")) return new BaseCrossField();
    return super.getField(name);
  }
  
  private static final Pattern PRI_ID_PTN = Pattern.compile("(\\S*)\\s+([A-Z]{3}\\d{12})");
  private class BasePriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PRI_ID_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strCallId = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PRI ID";
    }
  }
  
  private static final Pattern APT_PTN = Pattern.compile("\\b(?:APT|RM|UNIT) +([-A-Z0-9]+)$", Pattern.CASE_INSENSITIVE);
  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Matcher match = APT_PTN.matcher(field);
      if (match.find()) {
        apt = match.group(1);
        field = field.substring(0,match.start()).trim();
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, " - ", apt);
    }
  }
  
  private class BaseAptField extends AptField {
    
    @Override
    public void parse(String field, Data data) {
      data.strApt = append(data.strApt, "-", field);
    }
  }
  
  private class BasePlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("<UNKNOWN>")) return;
      if (field.startsWith("CALLBK=")) {
        data.strPhone = field.substring(7).trim();
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE PHONE";
    }
  }
  
  private class BaseTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) {
        data.strUnit = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override 
    public String getFieldNames() {
      return "TIME UNIT";
    }
  }
  
  private class BaseTimeInfoField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) {
        data.strSupp = append(data.strSupp, " / ", field.substring(pt+1).trim());
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override 
    public String getFieldNames() {
      return "TIME INFO";
    }
  }
  
  private class BaseCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("**")) field = field.substring(2).trim();
      data.strCall = field;
    }
  }
  
  private class BasePlace2Field extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (data.strPlace.length() == 0) {
        super.parse(field, data);
      } else {
        if (field.startsWith(data.strPlace)) {
          field = field.substring(data.strPlace.length()).trim();
          data.strSupp = append(data.strSupp, " / ", field);
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE INFO";
    }
  }
  
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("INCIDENT CLONED FROM ")) return;
      if (field.startsWith("Original Date/Time for ")) return;
      super.parse(field,  data);
    }
  }
  
  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (data.strUnit.contains("field")) return;
      if (data.strUnit.length() > 0 && field.contains(data.strUnit)) {
        data.strUnit = field;
      } else {
        data.strUnit = append(data.strUnit, " ", field);
      }
    }
  }
  
  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("UNKNOWN /")) field = field.substring(9).trim();
      super.parse(field, data);
    }
  }
}
