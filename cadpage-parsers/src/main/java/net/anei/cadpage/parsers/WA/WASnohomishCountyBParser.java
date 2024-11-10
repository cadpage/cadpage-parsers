package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WASnohomishCountyBParser extends FieldProgramParser {

  public WASnohomishCountyBParser() {
    super("SNOHOMISH COUNTY", "WA",
          "INFO? UNIT_CALL_CH  ( UNIT | CH? ) ADDRCITY1/S6 EMPTY? ( X_UNIT_INFO! | CH_INFO ) INFO/N+");
    setupSpecialStreets("AVE A");
    setupParseAddressFlags(FLAG_ALLOW_DUAL_DIRECTIONS);
  }

  @Override
  public String getFilter() {
    return "NoReply@snopac911.us,NoReply@snosafe.org,noreply@sno911.org";
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
    if (name.equals("UNIT_CALL_CH")) return new MyUnitCallChannelField();
    if (name.equals("UNIT")) return new UnitField("(?:\\*?\\b\\d?[A-Z]+\\d+[-\\d]*[A-Z]?\\b\\*?(?:, *)?)+", false);
    if (name.equals("CH")) return new ChannelField(CHANNEL_PTN_STR, true);
    if (name.equals("ADDRCITY1")) return new MyAddressCity1Field();
    if (name.equals("X_UNIT_INFO")) return new MyCrossUnitInfoField();
    if (name.equals("CH_INFO")) return new MyChannelInfoField();
    return super.getField(name);
  }

  private static final Pattern CALL_CHANNEL_PTN = Pattern.compile(">>>?([A-Z]+)<<<? *(.*)");
  private static final Pattern UNIT_CALL_PTN = Pattern.compile(">>>?(.*)<<<?([A-Z]+)");
  private class MyUnitCallChannelField extends Field {

    @Override public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith(">>") && !field.startsWith("<<")) return false;
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
  private static final Pattern MSPACE_PTN = Pattern.compile(" {3,}");
  private static final Pattern MAP_PTN = Pattern.compile("[A-Z]{2}\\d{3}[A-Z0-9]?");
  private class MyAddressCity1Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PLACE_PTN.matcher(field);
      if (match.matches()) {
        super.parse(match.group(1).trim().replace('@','&'), data);
        data.strPlace = match.group(2).trim();
        data.strMap = match.group(3).trim();
      } else {
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
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE MAP";
    }
  }

  private static final Pattern X_UNIT_INFO_PTN = Pattern.compile("(?:Between ([^*]*?))?\\*([ ,A-Z0-9]*)\\* *(.*)");
  private class MyCrossUnitInfoField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = X_UNIT_INFO_PTN.matcher(field);
      if (!match.matches()) return false;
      String cross = getOptGroup(match.group(1));
      if (!cross.equals("No Cross Streets Found")) {
        data.strCross = cross;
      }
      data.strUnit = append(data.strUnit, " ", match.group(2).trim());
      data.strSupp = append(data.strSupp, "\n", match.group(3).trim());
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
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
      data.strSupp = append(data.strSupp, "\n", field);
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
//
//  private static final Properties CALL_CODES = buildCodeTable(new String[]{
//      "ACTIVE",   "Active Shooter",
//      "AIR",      "Small Plane Crash",
//      "AIRC",     "Commercial Plane Crash",
//      "AIRS",     "Standby for Potential Plane Crash",
//      "BOMB",     "Bomb",
//      "FB",       "Brush Fire",
//      "FC",       "Commercial Fire",
//      "FCC",      "Fire Commercial Confirmed",
//      "FFB",      "Fire Boat Fire",
//      "FR",       "Residential Fire",
//      "FRC",      "Fire Residential Confirmed",
//      "HZ",       "HazMat",
//      "MCI",      "Mass Causality Incident"
//
//  });
}
