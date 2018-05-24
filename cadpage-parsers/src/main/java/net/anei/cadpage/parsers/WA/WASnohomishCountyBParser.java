package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WASnohomishCountyBParser extends FieldProgramParser {
  
  public WASnohomishCountyBParser() {
    super("SNOHOMISH COUNTY", "WA",
           "UNIT_CALL_CH CH? ADDRCITY/S6 MAP2/D? X_UNIT_INFO! INFO/S+");
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
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT_CALL_CH")) return new MyUnitCallChannelField();
    if (name.equals("CH")) return new ChannelField("\\*?((?:FIRE|NC F) TAC \\d+(?:/\\d+)?|) *[/*]?", true);
    if (name.equals("CALL")) return new CallField(">>([A-Z]+)<<", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("MAP2")) return new MapField("\\d{3}(?:[A-Z]\\d)?|", true);
    if (name.equals("X_UNIT_INFO")) return new MyCrossUnitInfoField();
    return super.getField(name);
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
      return "CALL CH";
    }
    
  }
  
  private static final Pattern ADDR_PLACE_PTN = Pattern.compile("([^/]*)/(.*?)/([^/]*)/?");
  private class MyAddressCityField extends AddressCityField {
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
