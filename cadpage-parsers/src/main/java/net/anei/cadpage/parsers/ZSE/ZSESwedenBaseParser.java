package net.anei.cadpage.parsers.ZSE;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZSESwedenBaseParser extends FieldProgramParser {

  public ZSESwedenBaseParser(String defCity, String defState, String program) {
    super(defCity, defState, CountryCode.SE, program);
  }

  public ZSESwedenBaseParser(String defCity, String defState, String program, int flags) {
    super(defCity, defState, CountryCode.SE, program, flags);
  }

  private Set<String> channelSet = new HashSet<String>();

  @Override
  protected boolean parseFields(String[] fields, Data data) {
    channelSet.clear();
    return super.parseFields(fields, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CH")) return new MyChannelField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" X ", " & ").replace(" x ", " & ");
      super.parse(field, data);
    }
  }

  private static final Pattern CHANNEL_PTN = Pattern.compile(".* (?:RAPS-|raps |SjvIns-|SamvFlyg-)\\d+|E-tunaIns");

  protected boolean isValidChannel(String field) {
    return CHANNEL_PTN.matcher(field).matches();
  }

  protected void addChannel(String field, Data data) {
    field = field.replace("raps ", "RAPS-");
    if (channelSet.add(field)) {
      data.strChannel = append(data.strChannel, " / ", field);
    }
  }

  private class MyChannelField extends ChannelField {
    public MyChannelField() {
      setPattern(CHANNEL_PTN, true);
    }

    @Override
    public void parse(String field, Data data) {
      addChannel(field, data);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("La = (\\d+)(?:[^\\p{ASCII}]+| grader| ) ([\\d\\.,]+)'([NS]) +Lo = *(\\d+)(?:[^\\p{ASCII}]+| grader| ) ([\\d\\.,]+)'([EW])");
  private class MyGPSField extends GPSField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      return parseGPSField(field, data);
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  protected boolean parseGPSField(String field, Data data) {
    if (field.length() == 0) return true;
    Matcher match = GPS_PTN.matcher(field);
    if (!match.matches()) return false;

    String gpsLoc = (match.group(3).charAt(0) == 'S' ? "-" : "+") + match.group(1) + ' ' + match.group(2) + ' ' +
                    (match.group(6).charAt(0) == 'W' ? "-" : "+") + match.group(4) + ' ' + match.group(5);
    gpsLoc = gpsLoc.replace(',', '.');
    setGPSLoc(gpsLoc, data);
    return true;
  }


  /**
   * Clean up extraneous line breaks from message body
   * @param body original message body
   * @return adjust message body
   */
  String cleanFixedLabelBreaks(String body) {
    int keyLen = body.indexOf(':');
    if (keyLen < 0) return null;
    return cleanFixedLabelBreaks(body, keyLen);
  }

  /**
   * Clean up extraneous line breaks from message body
   * @param body original message body
   * @param keyLen length of expected field label
   * @return adjust message body
   */
  String cleanFixedLabelBreaks(String body, int keyLen) {

    if (body.length() <= keyLen) return null;
    if (body.charAt(keyLen) != ':') return null;

    StringBuilder sb = null;
    int lastPt = 0;

    // OK, lets start through message
    for (int pos = 0; pos < body.length(); pos++) {

      // Everything is peachy until we find a line break;
      if (body.charAt(pos) == '\n') {

        // The only legitimate line breaks are followed by a colon
        // keylen characters ahead.  Not counting any intervening line breaks
        // which are not legitimate and will need to be removed.
        boolean removeBreak = false;
        int keyPt = pos+1;
        char chr = 0;

        for (int keyCnt = 0; keyCnt < keyLen; keyCnt++) {
          if (++keyPt >= body.length()) {
            chr = 0;
            break;
          }
          chr = body.charAt(keyPt);
          if (removeBreak && chr == ':') break;
          while (chr == '\n') {
            keyPt++;
            removeBreak = true;
            chr = (keyPt < body.length() ? body.charAt(keyPt) : 0);
          }
        }

        // If the last character was a colon, this was a legitimate line break and should
        // remain, and if no breaks were found in the  keyword, then nothing needs to be done
        // and we can just skip over the identified keyword
        if (chr == ':' && !removeBreak) {
          pos = keyPt;
        }

        // Otherwise, we are going to have to remove something.  Which means it is time
        // to update the StringBuilder object
        else {
          if (sb == null) {
            sb = new StringBuilder();
            lastPt = 0;
          }
          sb.append(body.substring(lastPt, pos));
          lastPt = pos;

          //  If we did not find a colon at the right spot, this is a bad line break
          // and we will simply skip over it
          if (chr != ':') {
            if (body.charAt(pos-1) != ' ') sb.append(' ');
            lastPt++;
          }

          // Otherwise, we need to remove any line breaks from the identified keyword
          // and skip over it
          else {
            sb.append('\n');
            sb.append(body.substring(pos+1, keyPt+1).replace("\n", ""));
            pos = keyPt;
            lastPt = keyPt+1;
          }
        }
      }
    }

    // We are done!!!
    // If not adjustments are required, return the original message
    if (sb == null) return body;

    // Otherwise close out the builder and return the adjusted message body
    sb.append(body.substring(lastPt));
    return sb.toString();
  }
}
