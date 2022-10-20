package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Process calls for Southern CAD software
 * with added ability to handle calls that have been split between the message subject and text body
 */
public class DispatchSouthernPlusParser extends DispatchSouthernParser {

  public DispatchSouthernPlusParser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState, DSFLAG_DISPATCH_ID);
  }

  public DispatchSouthernPlusParser(String[] cityList, String defCity, String defState, long flags) {
    super(cityList, defCity, defState, flags);
  }

  public DispatchSouthernPlusParser(String[] cityList, String defCity, String defState, long flags, String unitPtn) {
    super(cityList, defCity, defState, flags, unitPtn);
  }

  public DispatchSouthernPlusParser(CodeSet callSet, String[] cityList, String defCity, String defState, long flags) {
    super(callSet, cityList, defCity, defState, flags);
  }

  public DispatchSouthernPlusParser(CodeSet callSet, String[] cityList, String defCity, String defState, long flags, String unitPtn) {
    super(callSet, cityList, defCity, defState, flags, unitPtn);
  }

  public DispatchSouthernPlusParser(String[] cityList, String defCity, String defState, String program) {
    super(cityList, defCity, defState, program);
  }


  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    subject = subject.trim();
    boolean badTime = false;
    String callId = "";

    if (subject.length() > 0) {
      Matcher match = SUB_MARKER.matcher(body);
      if (match.find()) {
        String operId = match.group(1);
        if (operId != null) body = body.substring(match.end(1));
        if (!SUB_TRAILER.matcher(subject).find()) {
          if (subject.contains(",") && !subject.endsWith(",")) subject += ',';
          subject += " 00";
          badTime = true;
        }
        if (operId != null) subject = operId + subject;
        body = subject + ':' + body;
      }

      match = CALL_ID_PTN.matcher(body);
      if (match.find()) {
        callId = match.group(1);
        body = body.substring(0,match.start());
      }
    }

    if (!super.parseMsg(body, data)) return false;

    if (badTime) data.strTime = "";

    if (data.strCallId.length() == 0) data.strCallId = callId;
    return true;
  }

  private final static Pattern SUB_MARKER = Pattern.compile("^([A-Za-z0-9]+:)?\\d\\d:\\d\\d(?:[ ,;]|$)");
  private final static Pattern SUB_TRAILER = Pattern.compile("[ ,;]\\d\\d$");
  private final static Pattern CALL_ID_PTN = Pattern.compile(" +OCA: *(\\d\\d-\\d\\d-\\d{4})$");
}
