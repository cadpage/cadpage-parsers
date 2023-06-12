package net.anei.cadpage.parsers.MT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MTMissoulaCountyParser extends FieldProgramParser {

  public MTMissoulaCountyParser() {
    super("MISSOULA COUNTY", "MT",
          "Call_Type:CALL! Address:ADDRCITYST/S6 Common_Name:PLACE! Assigned_Units:UNIT! Narrative:INFO INFO/N+");
  }

  @Override
  public String getFilter() {
    return "911@missoulaonthealert.com,911Alerts@missoulacounty.us";
  }

  private static final Pattern MARKER = Pattern.compile("(?:(\\d{4}), )?Call # (\\d+) +");

  @Override
  protected boolean parseMsg(String body, Data data) {

    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    data.strSource = getOptGroup(match.group(1));
    data.strCallId = match.group(2);
    body = body.substring(match.end());

    int pt = body.indexOf("\nMessages and attachments");
    if (pt >= 0) body = body.substring(0,pt).trim();

    if (body.contains("\n")) {
      return parseFields(body.split("\n"), data);
    } else {
      return super.parseMsg(body, data);
    }
  }

  @Override
  public String getProgram() {
    return "SRC ID " + super.getProgram();
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    return super.getField(name);
  }

  private static final Pattern PRESERVE_NUMBER_PTN = Pattern.compile(".* MM HWY");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
      if (data.strApt.length() > 0 && PRESERVE_NUMBER_PTN.matcher(data.strAddress).matches()) {
        data.strAddress = data.strAddress + ' ' + data.strApt;
        data.strApt = "";
      }
    }
  }
}
