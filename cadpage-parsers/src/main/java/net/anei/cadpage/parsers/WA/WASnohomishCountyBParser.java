package net.anei.cadpage.parsers.WA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WASnohomishCountyBParser extends FieldProgramParser {
  
  public WASnohomishCountyBParser() {
    super("SNOHOMISH COUNTY", "WA",
          "( DATE:DATETIME_CH CALL ADDRCITY3/S6! UNIT " + 
          "| UNIT_CALL_CH  ( UNIT ADDRCITY2/S6 CH_INFO " + 
                         "| CH? ADDRCITY1/S6 MAP2/D? X_UNIT_INFO! " + 
                         ") " + 
          ") INFO/N+");
    setupSpecialStreets("AVE A");
    setupParseAddressFlags(FLAG_ALLOW_DUAL_DIRECTIONS);
  }
  
  @Override
  public String getFilter() {
    return "NoReply@snopac911.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("DISP")) return false;
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.strCall.equals("FR") || data.strCall.equals("FC")) {
      data.strCall = append(data.strCall, " ", data.strUnit);
    }
    return true;
  }
  
  private static final String CHANNEL_PTN_STR = "\\*?((?:FIRE|NC F) TAC \\d+(?:/\\d+)?|) *[/*]?";
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME_CH")) return new MyDateTimeChannelField();
    if (name.equals("UNIT_CALL_CH")) return new MyUnitCallChannelField();
    if (name.equals("UNIT")) return new UnitField("(?:\\b\\d?[A-Z]+\\d+[A-Z]?\\b(?:, *)?)+", false);
    if (name.equals("CH")) return new ChannelField(CHANNEL_PTN_STR, true);
    if (name.equals("ADDRCITY1")) return new MyAddressCity1Field();
    if (name.equals("ADDRCITY2")) return new MyAddressCity2Field();
    if (name.equals("ADDRCITY3")) return new MyAddressCity3Field();
    if (name.equals("MAP2")) return new MapField("\\d{3}(?:[A-Z]\\d)?|", true);
    if (name.equals("X_UNIT_INFO")) return new MyCrossUnitInfoField();
    if (name.equals("CH_INFO")) return new MyChannelInfoField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_CH_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d [AP]M) *(.*)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeChannelField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_CH_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
      data.strChannel = match.group(3);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME CH";
    }
  }
  
  private static final Pattern CALL_CHANNEL_PTN = Pattern.compile(">>([A-Z]+)<< *(.*)");
  private static final Pattern UNIT_CALL_PTN = Pattern.compile(">>(.*)<<([A-Z]+)");
  private class MyUnitCallChannelField extends Field {
    
    @Override public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher  match = CALL_CHANNEL_PTN.matcher(field);
      if (match.matches()) {
        data.strCall = match.group(1);
        data.strChannel = match.group(2);
      } else if ((match = UNIT_CALL_PTN.matcher(field)).matches()) {
        data.strUnit = match.group(1).trim();
        data.strCall =  match.group(2).trim();
      } else {
        field = stripFieldStart(field, "<<");
        field = stripFieldEnd(field, ">>");
        data.strCall = field;
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CALL CH?";
    }
    
  }
  
  private static final Pattern ADDR_PLACE_PTN = Pattern.compile("([^/]*)/(.*?)/([^/]*)/?");
  private class MyAddressCity1Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PLACE_PTN.matcher(field);
      if (!match.matches()) abort();
      super.parse(match.group(1).trim().replace('@','&'), data);
      data.strPlace = match.group(2).trim();
      data.strMap = match.group(3).trim();
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE MAP";
    }
  }
  
  private static final Pattern MSPACE_PTN = Pattern.compile(" {3,}");
  private static final Pattern MAP_PTN = Pattern.compile("[A-Z]{2}\\d{4}");
  private class MyAddressCity2Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String[] parts = MSPACE_PTN.split(field);
      super.parse(parts[0].replace('@','&'), data);
      if (parts.length > 3) abort();
      if (parts.length == 3) {
        data.strPlace = parts[1];
        data.strMap = parts[2];
      } else if (parts.length == 2) {
        if (MAP_PTN.matcher(parts[1]).matches()) {
          data.strMap = parts[1];
        } else {
          data.strPlace = parts[1];
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE MAP";
    }
  }
  
  private static final Pattern ADDR_CITY_PTN3 = Pattern.compile("(.*?) {3,}<<(.*)>>");
  private class MyAddressCity3Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_PTN3.matcher(field);
      if (!match.matches()) abort();
      super.parse(match.group(1), data);
      data.strPlace = match.group(2);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private static final Pattern X_UNIT_INFO_PTN = Pattern.compile("(?:Between ([^*]*?))?\\*([ ,A-Z0-9]*)\\* *(.*)");
  private class MyCrossUnitInfoField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = X_UNIT_INFO_PTN.matcher(field);
      if (!match.matches()) abort();
      String cross = getOptGroup(match.group(1));
      if (!cross.equals("No Cross Streets Found")) {
        data.strCross = cross;
      }
      data.strUnit = append(data.strUnit, " ", match.group(2).trim());
      data.strSupp = match.group(3).trim();
    }

    @Override
    public String getFieldNames() {
      return "X UNIT INFO";
    }
  }

  private static final Pattern CHANNEL_PTN = Pattern.compile(CHANNEL_PTN_STR);
  private class MyChannelInfoField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CHANNEL_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strChannel = match.group(1);
        field = field.substring(match.end()).trim();
      }
      data.strSupp = field;
    }
    
    @Override
    public String getFieldNames() {
      return "CH INFO";
    }
  }
  
  private static final Pattern APT_DIROF_PTN = Pattern.compile("[NSEW]O .*");
  
  @Override
  protected boolean isNotExtraApt(String apt) {
    if (APT_DIROF_PTN.matcher(apt).matches()) return true;
    return super.isNotExtraApt(apt);
  }

  @Override
  public String adjustMapAddress(String addr) {
    // Usually PK means PIKE, but not here
    addr = PK_PTN.matcher(addr).replaceAll("PKWY");
    
    // SR 2 usually means WA 2, but here is it US 2
    addr = SR2_PTN.matcher(addr).replaceAll("US 2");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern PK_PTN = Pattern.compile("\\bPK\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern SR2_PTN = Pattern.compile("\\bSR *2\\b", Pattern.CASE_INSENSITIVE);
}
