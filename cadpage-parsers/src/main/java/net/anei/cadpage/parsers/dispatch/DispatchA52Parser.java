package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA52Parser extends FieldProgramParser {

  private Properties callCodes;
  private CodeTable callTable;

  public DispatchA52Parser(String defCity, String defState) {
    this(null, null, null, defCity, defState);
  }

  public DispatchA52Parser(Properties callCodes, String defCity, String defState) {
    this(callCodes, null, null, defCity, defState);
  }

  public DispatchA52Parser(CodeTable codeTable, String defCity, String defState) {
    this(null, codeTable, null, defCity, defState);
  }

  public DispatchA52Parser(Properties callCodes, Properties cityCodes, String defCity, String defState) {
    this(callCodes, null, cityCodes, defCity, defState);
  }

  public DispatchA52Parser(CodeTable codeTable, Properties cityCodes, String defCity, String defState) {
    this(null, codeTable, cityCodes, defCity, defState);
  }

  private DispatchA52Parser(Properties callCodes, CodeTable callTable, Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
          "TYP:CODE1 MODCIR:CODE2 TYPEN:CALL TYPN:CALL CC_TEXT:CALL LOC:ADDR! BLD:APT FLR:APT APT:APT AD:PLACE DESC:PLACE CITY:CITY ZIP:ZIP CRSTR:X UNS:UNIT TIME:DATETIME3 INC:ID GRIDREF:MAP " +
          "CMT:INFO/N PROBLEM:INFO/N CC:SKIP CASE__#:ID PRIORITY:PRI CALLER:NAME LOCDESC:NAME USER_ID:SKIP CREATED:SKIP RFD:SKIP LOCATION:SKIP LAT:GPS1 LONG:GPS2", FLDPROG_ANY_ORDER | FLDPROG_IGNORE_CASE);

    this.callCodes = callCodes;
    this.callTable = callTable;
  }

  private static final Pattern PREFIX_PTN = Pattern.compile("[a-z0-9]*: *");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    Matcher match = PREFIX_PTN.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());
    if (body.startsWith("LOC:APPROX LOC:")) body = body.substring(11);
    if (!body.startsWith("LOC:") && !body.startsWith("TYPN")) return false;
    body = body.replace("LOC DESC:", "LOCDESC:");
    if (!super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) return false;
    if (data.strAddress.length() == 0 && data.strGPSLoc.length() > 0) {
      int pt = data.strGPSLoc.indexOf(',');
      data.strAddress = "LAT:" + data.strGPSLoc.substring(0,pt) + " LONG:" + data.strGPSLoc.substring(pt+1);
      data.strGPSLoc = "";
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CODE", "CODE CALL?");
  }

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("EEEEEE, MMMM dd, yyyy hh:mm:ss aa");

  @Override
  public Field getField(String name) {
    if (name.equals("CODE1")) return new MyCode1Field();
    if (name.equals("CODE2")) return new MyCode2Field();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("DATETIME3")) return new DateTimeField(DATE_TIME_FMT);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ZIP")) return new MyZipField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private class MyCode1Field extends CodeField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "*M*");
      field = stripFieldEnd(field, "-");
      super.parse(field, data);
      if (callCodes != null) {
        String call = callCodes.getProperty(data.strCode);
        if (call != null) {
          data.strCall = call;
        } else {
          data.strCall = data.strCode;
        }
      } else if (callTable != null) {
        String call = callTable.getCodeDescription(data.strCode);
        if (call != null) {
          data.strCall = call;
        } else {
          data.strCall = data.strCode;
        }
      } else {
        data.strCall = data.strCode;
        data.strCode = "";
      }
    }
  }

  private class MyCode2Field extends CodeField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;

      if (getRelativeField(-1).startsWith("TYPN:")) {
        data.strCode = field;
      }
      else if (data.strCode.length() == 0) {
        data.strCall = append(data.strCall, " ", field);
      }
      else if (data.strCall.equals(data.strCode)) {
        data.strCode = data.strCall = append(data.strCode, " ", field);
      } else {
        data.strCode = append(data.strCode, " ", field);
      }
    }
  }

  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      field = MSPACE_PTN.matcher(field).replaceAll(" ");
      if (data.strCode.length() == 0) {
        data.strCode = data.strCall;
        data.strCall = field;
      } else if (data.strCode.equals(data.strCall)) {
        data.strCall = field;
      } else if (data.strCall.length() == 0) {
        data.strCall = field;
      } else if (field.startsWith(data.strCall)) {
        data.strCall = field;
      } else if (data.strCall.startsWith(field)) {
      } else {
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "CALL INFO?";
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (data.strAddress.equals("APPROX")) {
        field = append("(APPROX)", " ", field);
        data.strAddress = "";
      }
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("/")) field = field.substring(0,field.length()-1).trim();
      if (field.startsWith("/")) field = field.substring(1).trim();
      super.parse(field, data);
    }
  }

  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCallId.length() > 0) return;
      super.parse(field, data);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('/');
      if (pt >= 0) field = field.substring(pt+1).trim();
      super.parse(field, data);
    }
  }

  private class MyZipField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      super.parse(field, data);
    }
  }

  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (field.equals(data.strMap)) return;
      data.strMap = append(data.strMap, "-", field);
    }
  }

  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
    }

    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "<");
      field = stripFieldEnd(field, ">");
      super.parse(field, data);
    }
  }
}
