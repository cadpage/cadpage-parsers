package net.anei.cadpage.parsers;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class Message {

  // pre-parse message information
  private String parseAddress;
  private String parseSubject;
  private String parseSubjectFollow = null;
  private String parseMessageBody;
  private String parseMessageBodyFollow = null;
  private int leadBrkCount = 0;
  private int msgIndex = -1;
  private int msgCount = -1;
  private int origMsgLen = -1;

  private MsgInfo info = null;

  public Message(boolean preParse, String fromAddress, String subject, String body) {
    this(preParse, fromAddress, subject, body, null);
  }

  private static final Pattern TRAIL_LF_PTN = Pattern.compile("[\r\n]+$");

  public Message(boolean preParse, String fromAddress, String subject, String body, SplitMsgOptions options) {
    if (fromAddress == null) fromAddress = "";
    if (subject == null) subject = "";
    if (body == null) body = "";
    if (options == null) options = new SplitMsgOptionsCustom();
    body = TRAIL_LF_PTN.matcher(body).replaceFirst("");
    origMsgLen = body.length();

    // Remove byte order mark from anywhere in text.  Generally it only occurs
    // at the beginning, but we have had a case where a prefix was prepended to
    // a string starting with a byte order mark, so get rid of it wherever it
    // might occur
    body = body.replace("\uFEFF", "");


    // Decode base64 alerts
    if (body.startsWith("77u/")) {
      body = body.substring(4);
      try {
        byte[] tmp1 = body.getBytes("UTF-8");
        byte[] tmp2 = Base64.decodeBase64(tmp1);
        body = new String(tmp2);
      } catch (UnsupportedEncodingException ex) {}
    }

    // Replace some odd unicode characters with their closest ASCII equivalents
    body = demimic(body);

    if (options.subjectColonField()) {
      this.parseAddress = fromAddress;
      this.parseSubject = "";
      if (subject.length() > 0) body = (subject + ": " + body).trim();
      this.parseMessageBody = body;
    }

    else if (! preParse) {
      this.parseAddress = fromAddress;
      this.parseSubject = subject;
      this.parseMessageBody = finish(body, options);
    }

    else {
      preParse(fromAddress, subject, body, options);
    }
  }

  public String demimic(String body) {
    StringBuilder sb = new StringBuilder();
    for (char chr : body.toCharArray()) {
      switch (Character.getType(chr)) {

      case Character.SPACE_SEPARATOR:
        chr = ' ';
        break;

      case Character.DASH_PUNCTUATION:
        chr = '-';
        break;

      case Character.LINE_SEPARATOR:
      case Character.PARAGRAPH_SEPARATOR:
        chr = '\n';
        break;
      }
      sb.append(chr);
    }
    return sb.toString();
  }

  public String getFromAddress() {
    return parseAddress;
  }

  public String getSubject() {
    return parseSubject;
  }

  public String getSubject(boolean follow) {
    if (msgIndex > 0) follow = (msgIndex > 1);
    if (follow && parseSubjectFollow != null) return parseSubjectFollow;
    return parseSubject;
  }

  public void setMessageBody(String body) {
    parseMessageBody = body;
    info = null;
  }

  public String getMessageBody() {
    return getMessageBody(false, false);
  }

  public String getMessageBody(boolean keepLeadBreak) {
    return getMessageBody(keepLeadBreak, false);
  }

  public String getMessageBody(boolean keepLeadBreak, boolean follow) {
    if (msgIndex > 0) follow = (msgIndex > 1);
    String body = follow && parseMessageBodyFollow != null ? parseMessageBodyFollow : parseMessageBody;
    if (!keepLeadBreak || leadBrkCount == 0) return body;
    StringBuilder sb = new StringBuilder();
    for (int j = 0; j<leadBrkCount; j++) sb.append('\n');
    sb.append(body);
    return sb.toString();
  }

  public int getMsgIndex() {
    return msgIndex;
  }

  public int getMsgCount() {
    return msgCount;
  }

  public int getOrigMsgLen() {
    return origMsgLen;
  }

  void setOrigMsgLen(int origMsgLen) {
    this.origMsgLen = origMsgLen;
  }

  /**
   * Called by test code to retrieve value set as the location code
   * @return location code saved by setLocationCode
   */
  protected String getLocationCode() {
    if (info == null) return null;
    return info.getParserCode();
  }

  // Patterns used to perform front end descrambling
  private static final Pattern SUBJECT_EMAIL_PTN = Pattern.compile("Forwarded Message from ([^ ]+)");
  private static final Pattern RETURN_PTN = Pattern.compile("\r+\n|\n\r+|\r");
  private static final Pattern LEAD_BLANK = Pattern.compile("^ *\" \":? +");
  private static final Pattern FORWARD_PTN = Pattern.compile("(?:FWD?|Begin forwarded message): *");
  private static final Pattern LEAD_UNDERSCORE_PTN = Pattern.compile("\n*_{5,}\n+");
  private static final Pattern DISCLAIMER_PTN = Pattern.compile("\n+DISCLA| *\\[Attachment\\(s\\) removed\\]\\s*$|\n+To unsubscribe ", Pattern.CASE_INSENSITIVE);
  private static final Pattern FWD_PTN = Pattern.compile("^FWD?:");
  private static final Pattern[] TRAIL_MSG_HEADER_PTNS = new Pattern[]{
    Pattern.compile(" *\\[(\\d) of (\\d)\\]\\s*$"),
    Pattern.compile(":(\\d)of(\\d)\\s*$"),
    Pattern.compile("_(\\d) of (\\d)\\s*$"),
    Pattern.compile(" \\(0(\\d)/0(\\d)\\)\\s*$"),
    Pattern.compile("\\(Part (\\d) of (\\d)\\)? +POR[A-Z0-9]+FIRE$"),
    Pattern.compile("\\(Part (\\d) of (\\d)\\)")
  };
  private static final Pattern[] MSG_HEADER_PTNS = new Pattern[]{
    Pattern.compile("^(000\\d)/(000\\d)\\b"),
    Pattern.compile("^(?:MACON CO 911: *)?(\\d) *of *(\\d):"),
    Pattern.compile("^ *(\\d)/(\\d)(?: / |\n\n|:)"),
    Pattern.compile("^([-\\w\\.]+@[\\w\\.]+) /(\\d)/(\\d) /"),
    Pattern.compile("^([-\\w\\.]+@[\\w\\.]+) / ([A-Za-z0-9 ]*?) / (\\d)/(\\d) +"),
    Pattern.compile("^Dispatch / (\\d)/(\\d) +"),
    Pattern.compile("^(\\d)/(\\d)\n+"),
    Pattern.compile("^(\\d)/(\\d)(?![/\\d])"),
    Pattern.compile("^(?:\\(Con't\\) )?(\\d) of (\\d)\n"),
    Pattern.compile("[ A-Z]+ CAD: *(\\d)of(\\d):"),
    Pattern.compile("^(\\d)/(\\d)(?=\\d{3,}:)"),  // This one is scarry !!!
    Pattern.compile("^[A-Z]+ +\\((\\d)/(\\d)\\) +(.*?) +STOP\\s*$"),
    Pattern.compile("^\\( *([^\\)]*?) +(\\d) *of *(\\d)\\)(.*)\\s*$", Pattern.DOTALL)
  };
  private static final Pattern MSG_HEADER_FINAL_PTN = Pattern.compile("^(\\d)/(\\d)[: ] *");
  private static final Pattern[] SUBJECT_HEADER_PTNS = new Pattern[]{
    Pattern.compile("^(\\d)/(\\d)$"),
    Pattern.compile("^(\\d) *of *(\\d)\\b *"),
    Pattern.compile(" *(?:- part )?\\b(\\d) *of *(\\d)(?: Texts)?$"),
    Pattern.compile("^\\[(\\d)(?:/|of| +of +)(\\d)\\] *"),
    Pattern.compile("^\\((\\d)(?:/|of| +of +)(\\d)\\) *"),
    Pattern.compile(" *\\[(\\d)(?:/|of| +of +)(\\d)\\]$"),
  };
  private static final Pattern OPT_OUT_PTN = Pattern.compile("TXT STOP.*$");
  private static final Pattern CONT_PTN = Pattern.compile(" ?\\(C.* \\d\\d? of \\d\\d?");

  private static final Pattern[] EMAIL_PATTERNS = new Pattern[]{
    Pattern.compile("^(?:\\*.*\\*)?([-\\w\\.]+@[-\\w]+\\.[-\\w\\.]+)(?: +/ +(?:no subject +)?/ +)"),
    Pattern.compile(" - Sender: *([-\\w\\.]+@[-\\w\\.]+) *\n"),
    Pattern.compile("^(?:[-=.+_a-z0-9]*[0-9a-f]{8,}[-=.+_a-z0-9]*=)?((?:[\\w.!\\-]+\\w|\\\"[\\w\\.!\\- ]+\\\")@[-\\w]+\\.[-\\w\\.]+)[\\s:]"),
    Pattern.compile("^((?:[-\\w\\.]+)@[-\\w]+) *+(?=\\()"),
    Pattern.compile("^\\*\\d+: \\*([-\\w]+@[-\\w\\.]+) +"),
    Pattern.compile("^[^\n]*\\bFr: *(\\S+@\\S+)\\s+"),
    Pattern.compile("^From: *(\\S+@\\S+) +(?:Msg: *)?"),
    Pattern.compile("sentto-[-\\d]+ *= *([-\\.\\w]+@[-\\.\\w]+) +"),
    Pattern.compile("^[\\d\\.]+=([-_a-z0-9\\.]+@[-_a-z0-9\\.]+) +"),
    Pattern.compile("SRS1=[A-Za-z0-9\\.]+=AIDz=WT=[A-Za-z0-9\\.]+= ([A-Za-z0-9\\.]+@[A-Za-z0-9\\.]+)"),
    Pattern.compile("FRM: *(.*?) MSG: *")
  };
  private static final Pattern EMAIL_PFX_PATTERN = Pattern.compile("^([-\\w\\.]+@\\w+\\.[-\\w\\.]+)(?:\\n|: )");
  private static final Pattern FRM_TAG_PATTERN = Pattern.compile("\n *FRM:");
  private static final Pattern MULTI_MSG_MASTER_PATTERN = Pattern.compile("1 of \\d FRM:(.*?) SUBJ:(.*?) MSG:(.*)\\(End\\)");
  private static final Pattern MULTI_MSG_BREAK_PATTERN = Pattern.compile(" \\(Con't\\) \\d of \\d ");
  private static final Pattern[] E_S_M_PATTERNS = new Pattern[]{
    Pattern.compile("^(?:([-\\w\\.]+@[-\\w\\.]+) +)?Subj(?:ect)?: *(.*)\n"),
    Pattern.compile("^(?:([^ ,;/]+) +)?S:(.*?)(?: +M:|\n)"),
    Pattern.compile("^Fr:<(.*?)>?\nSu:(.*?)\nTxt: "),
    Pattern.compile("From: *(.*) Subj: *(.*) Msg: *"),
    Pattern.compile("^prvs=[0-9a-f]{8,}=[\\w .<>@]*<([\\w.\\-]+@[\\w.]+)> *\\((.*?)\\)"),
    Pattern.compile("^([-\\w\\.]+@[-\\w\\.]+)/([-_ A-Za-z0-9]+)/ *"),
  };
  private static final Pattern[] S_M_PATTERNS = new Pattern[]{
    Pattern.compile("^SUBJ: *(.*)\n(?:MSG:)?"),
  };
  private static final Pattern LEAD_JUNK_PTN = Pattern.compile("no subject / |Failed to fetch website: |WARNING: The sender of this email could not be validated.*?\n+");

  private void preParse(String fromAddress, String subject, String body, SplitMsgOptions options) {

    boolean keepLeadBreak = options.splitKeepLeadBreak();

    // default address and subject to obvious values
    parseSubject = "";
    Matcher match = SUBJECT_EMAIL_PTN.matcher(subject);
    if (match.matches()) {
      parseAddress = match.group(1);
    } else {
      parseAddress = fromAddress;
      addSubject(subject);
    }

    // Get rid of any \r characters
    body = RETURN_PTN.matcher(body).replaceAll("\n");

    // Start by decoding common HTML sequences
    body = trimLead(body, keepLeadBreak);

    // Change spurious '¡' characters back to the @ there were originally intended to be
    body = body.replace('¡', '@');

    // Remove trailing disclaimer(s)
    match = DISCLAIMER_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();

    // See if we can find a trailing message index/count indicator
    // which may determine whether or not we clean the leading subject constructs
    // from the front of this message
    boolean trailIndexFound = false;
    match = findPattern(body, TRAIL_MSG_HEADER_PTNS);
    if (match != null) {
      trailIndexFound = true;
      msgIndex = Integer.parseInt(match.group(1));
      msgCount = Integer.parseInt(match.group(2));
      body = trimLead(body.substring(0,match.start()),keepLeadBreak);
    }

    body = cleanParenSubject(body, options);

    // Drop FWD: prefix
    match = FORWARD_PTN.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end()).trim();

    // Drop leading underscore line
    match = LEAD_UNDERSCORE_PTN.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());

    // See if we can parse this as an Email message header
    if (parseEmailHeaders(body, keepLeadBreak)) return;

    body = trimLead(body, keepLeadBreak);
    if (body.startsWith("Pagecopy-")) body = body.substring(9);

    // If we did not find a trailing index indicator, see if we can find
    // any leading index indicators
    if (!trailIndexFound) {
      match = findPattern(body, MSG_HEADER_PTNS);
      if (match != null) {
        boolean pinned = (match.groupCount() >= 3 && match.start() == 0 && match.end() == body.length());
        int ndx = 1;
        if (pinned) {
          if (match.groupCount() == 4) {
            String tmp = match.group(ndx++);
            if (!tmp.equals("- part")) addSubject(tmp);
          }
        }
        else if (match.groupCount() >= 3) {
          parseAddress = match.group(ndx++);
          if (match.groupCount() == 4) {
            String tmp = match.group(ndx++);
            if (tmp != null) addSubject(tmp);
          }
        }
        msgIndex = Integer.parseInt(match.group(ndx++));
        msgCount = Integer.parseInt(match.group(ndx++));
        if (pinned) {
          body = match.group(ndx);
        }
        else {
          String origBody = body;
          body = trimLead(body.substring(match.end()),keepLeadBreak);
          if (origBody.startsWith("((")) {
            if (body.startsWith(")")) body = trimLead(body.substring(1), keepLeadBreak);
            else body = "(" + body;
          }
        };
      } else {
        if (body.startsWith("/ ")) body = trimLead(body.substring(2), keepLeadBreak);
      }
    }

    // Get rid of leading quoted blanks
    match = LEAD_BLANK.matcher(body);
    if (match.find()) body = trimLead(body.substring(match.end()), keepLeadBreak);

    // And trailing opt out message
    match = OPT_OUT_PTN.matcher(body);
    if (match.find()) body = trimLead(body.substring(0,match.start()), keepLeadBreak);

    // Dummy loop we can break out of
    do {

      /* Decode patterns that look like this.....
      1 of 3
      FRM:CAD@livingstoncounty.livco
      SUBJ:DO NOT REPLY
      MSG:CAD:FYI: ;CITAF;5579 E GRAND RIVER;WILDWOOD DR;Event spawned from CITIZEN ASSIST LAW. [12/10/10
      (Con't) 2 of 3
      20:08:59 SPHILLIPS] CALLER LIVES NEXT DOOR TO THE ADDRESS OF THE WATER MAINBREAK [12/10/10 20:04:40 HROSSNER] CALLER ADV OF A WATER MAIN
      (Con 3 of 3
      BREAK(End)

      Or This

      FRM:e@fireblitz.com <Body%3AFRM%3Ae@fireblitz.com>
      MSG:48: TOWNHOUSE FIRE
      E818 BO802
      9903 BREEZY KNOLL CT [DEAD END & GREEN HAVEN RD]
      12/23 23:32
      http://fireblitz.com/18/8.shtm
      */
      int pt1 = -1;
      if (body.startsWith("FR:")) {
        pt1 = 3;
      }
      else if (body.startsWith("FRM:")) {
        pt1 = 4;
      } else if (EMAIL_PFX_PATTERN.matcher(body).find()) {
        pt1 = 0;
      } else {
        match = FRM_TAG_PATTERN.matcher(body);
        if (match.find()) pt1 = match.end();
      }
      if (pt1 >= 0) {
        msgIndex = msgCount = -1;
        String[] lines = body.substring(pt1).split("\n");
        if (lines.length > 1) {
          lines[0] = lines[0].trim();
          int ndx = 1;
          String tmp = lines[1].trim();
          if (tmp.startsWith("SUBJ:")) {
            lines[1] = tmp.substring(5).trim();
            ndx++;
          } else if (tmp.startsWith("SUB:")) {
            lines[1] = tmp.substring(4).trim();
            ndx++;
          }
          if (lines.length > ndx) {
            String line = lines[ndx];
            while (line.length() > 0 && line.charAt(0)==' ') line = line.substring(1);
            if (line.startsWith("MSG:")) {
              parseAddress = lines[0];
              if (ndx > 1) addSubject(lines[1]);
              StringBuilder sb = new StringBuilder(trimLead(line.substring(4), keepLeadBreak));
              boolean skipBreak = false;
              for ( ndx++; ndx < lines.length; ndx++) {
                line = lines[ndx];
                if (CONT_PTN.matcher(line).matches()) {
                  skipBreak = true;
                } else {
                  if (sb.length() > 0) {
                    if (!skipBreak) sb.append('\n');
                    else if (options.splitBreakIns()) sb.append('\n');
                    else if (options.splitBlankIns()) sb.append(' ');
                  }
                  sb.append(line);
                  skipBreak = false;
                }
              }
              trimLast(sb, "(End)");
              trimLast(sb, "\nMore?");
              body = trimLead(sb.toString(), keepLeadBreak);
              break;
            }
          }
        }
      }

      /* Decode patterns that look like this (same as above with line breaks removed)
        1 of 2 FRM:ics.gateway@wylietexas.gov SUBJ:Message From Wylie MSG:12030523 RESCUE-TRAPPED PERSON(S) TONY LN / N HIGHWAY 66 IN FATE [FAFD (Con't) 2 of 2 GRID: F121] UNITS: FATEFD ROMED1 ST RMK: <NONE> CFS RMK 12:48 VEHICLE ON ITS SIDE AND OCCP TRAPPED {WYPCCOM05 12:48}(End)
      */
      match = MULTI_MSG_MASTER_PATTERN.matcher(body);
      if (match.matches()) {
        parseAddress = match.group(1).trim();
        parseSubject = match.group(2).trim();
        body = MULTI_MSG_BREAK_PATTERN.matcher(match.group(3)).replaceAll(" ").trim();
        break;
      }

      /* Decode patterns that look like this
        Dispatch@ci.waynesboro.va.us <Body%3ADispatch@ci.waynesboro.va.us> Msg: Dispatch:2ND CALL 1001 HOPEMAN PKWY, ZAP12 INJURIES FROM PREVIOUS MVA
      */
      int ipt = body.indexOf(" Msg:");
      if (ipt >= 0) {
        String addr = body.substring(0,ipt).trim();
        if (addr.contains("@") && ! addr.contains(":")) {
          parseAddress = addr;
          body = trimLead(body.substring(ipt+5), keepLeadBreak);
          break;
        }
      }

      /* Decode patterns that look like this
       * "HC@hamilton-co.org\nMSG:\nHC:ODOR OF GAS 393 PROVIDENCE WY SHRN NEXT TO TRAILER..... CHARLES SOILBACK ** SMELL OF GAS ** SEE MALE COMPL REF ODOR OF NATURAL GAS LEAK FROM A POSS 1 INCH PIPE COMIN",
       */
      ipt  = body.indexOf("\nMSG:\n");
      if (ipt >= 0) {
        parseAddress = body.substring(0,ipt);
        body = trimLead(body.substring(ipt+6), keepLeadBreak);
        break;
      }

      /* Decode patterns that contain an email address, subject, and message
       * S:subject M:msg
       */
      match = findPattern(body, E_S_M_PATTERNS);
      if (match != null) {
        String from = match.group(1);
        if (from != null) parseAddress = from.trim();
        String sub = match.group(2);
        if (sub != null) addSubject(sub.trim());
        body = trimLead(body.substring(match.end()), keepLeadBreak);
        if (from != null) break;
      }

      /* Decode patterns that contain an subject and message
       */
      else {
        match = findPattern(body, S_M_PATTERNS);
        if (match != null) {
          String sub = match.group(1);
          if (sub != null) addSubject(sub.trim());
          body = trimLead(body.substring(match.end()), keepLeadBreak);
          break;
        }
      }


      /* Decode patterns that match EMAIL_PATTERN, which is basically an email address
       * followed by one of a set of known delimiters
       */
      match = findPattern(body, EMAIL_PATTERNS);
      if (match != null) {
        parseAddress = match.group(1);
        body = trimLead(body.substring(match.end()), keepLeadBreak);
        break;
      }

    } while (false);

    // Clean up general leading junk
    match = LEAD_JUNK_PTN.matcher(body);
    if (match.lookingAt()) body = trimLead(body.substring(match.end()), keepLeadBreak);

    body = finish(body, options);

    // Make one last check for a message index that might have been masked
    // by parenthesized subjects
    if (msgIndex < 0 && parseSubject.length() > 0) {
      match = MSG_HEADER_FINAL_PTN.matcher(body);
      if (match.find()) {
        msgIndex = Integer.parseInt(match.group(1));
        msgCount = Integer.parseInt(match.group(2));
        body = body.substring(match.end());
      }
    }
    parseMessageBody = body;

    // If we extracted an empty address from the text string, restore the
    // original address
    if (parseAddress.length() == 0) parseAddress = fromAddress;
  }


  /**
   * Try to parse message an an email message with regular email headers
   * @param body message body
   * @return true if successfully parsed as an email message
   */
  private boolean parseEmailHeaders(String body, boolean keepLeadBreak) {

    String address = "";
    String subject = "";
    int headerCnt = 0;

    int spt = 0;
    boolean extFrom = false;
    while (true) {
      while (spt < body.length() && Character.isWhitespace(body.charAt(spt))) spt++;
      int ept = spt;
      while (ept < body.length() && body.charAt(ept) != '\n') ept++;
      if (ept == body.length()) break;

      String line = body.substring(spt, ept).trim();
      if (extFrom && line.endsWith(">")) {
        address = address + line;
        extFrom = false;
      } else {
        extFrom = false;
        Matcher match;
        if (!JUNK_HEADER_PTN.matcher(line).matches()) {
          if ((match = SENDER_HEADER_PTN.matcher(line)).matches()) {
            address = match.group(1);
            if (!address.endsWith(">") && address.contains("<")) extFrom = true;
          } else if ((match = SUBJECT_HEADER_PTN.matcher(line)).matches()) {
            subject = match.group(1);
          } else if (!OTHER_HEADER_PTN.matcher(line).matches()) {
            break;
          }
          headerCnt++;
        }
      }
      spt = ept+1;
    }

    if (headerCnt < 3) return false;
    if (address.length() > 0) parseAddress = address;
    if (subject.length() > 0) parseSubject = subject;
    parseMessageBody = trimLead(body.substring(spt), keepLeadBreak);
    return true;
  }
  private static final Pattern SENDER_HEADER_PTN = Pattern.compile("(?:From|Sender): *(.*)");
  private static final Pattern SUBJECT_HEADER_PTN = Pattern.compile("(?:Subject): *(.*)");
  private static final Pattern OTHER_HEADER_PTN = Pattern.compile("\\[?mailto:.*\\]|(?:Content-Type|Date|Importance|Reply-To|Return-Path|Sent|SentTo|To|X-Mailer):.*");
  private static final Pattern JUNK_HEADER_PTN = Pattern.compile("_{5,}|-{5,}|--+(?:Original Message)?--+|Auto forwarded by a Rule|--+ Forwarded message --+");

  private String trimLead(String str, boolean keepLeadBreak) {
    if (keepLeadBreak) return str;
    int pt = 0;
    while (pt < str.length()) {
      char chr = str.charAt(pt);
      if (!Character.isWhitespace(chr)) break;
      if (chr != ' ') {
        if (keepLeadBreak) break;
        if (chr == '\n') leadBrkCount++;
      }
      pt++;
    }
    return str.substring(pt);
  }

  /**
   * Perform final message parsing.  This is the last preparse steps that should
   * be done even when no preparsing is requested.  It is used to back out
   * steps that might be generated by a message forwarder or might have been
   * included in the original message so we get consistent results
   * @param body message body
   * @param options split message processing options
   * @return adjusted message body
   */
  private String finish(String body, SplitMsgOptions options) {

    body = cleanParenSubject(body, options);

    Matcher match = EMAIL_PFX_PATTERN.matcher(body);
    if (match.find()) {
      parseAddress = match.group(1).trim();
      body = trimLead(body.substring(match.end()), true);
    }

    if (body.startsWith("MSG:")) body = trimLead(body.substring(4), true);

    match = FWD_PTN.matcher(parseSubject);
    if (match.find()) parseSubject = parseSubject.substring(match.end()).trim();

    // Last check, if we ended up with no message, use the last subject as the message
    if (body.length() == 0) {
      int pt = parseSubject.lastIndexOf('|');
      if (pt >= 0) {
        body = parseSubject.substring(pt+1);
        parseSubject = parseSubject.substring(0,pt);
      } else {
        body = parseSubject;
        parseSubject = "";
      }
    }

    parseSubject = MsgParser.decodeHtmlSequence(parseSubject);
    return body;
  }

  private String cleanParenSubject(String body, SplitMsgOptions options) {
    // Finally, leading values in square or round brackets are turned into
    // message subjects.  There may be more than one of these, in which case
    // the will be accumulated in parseSubject separated by pipe symbols

    // If the split message option to not parse subjects in following messages is
    // enabled, then all of this logic should be suppressed for any messages in
    // an bundle following the first one.  The problem is, we do not know for sure
    // at this time whether this is a following message or not.  We solve this by
    // saving the unprocessed message in subject in a separate variable to be used
    // if we later determine this is a following message, then process things normally
    boolean saveFollow = options.noParseSubjectFollow() && parseMessageBodyFollow == null;
    if (saveFollow) {
      parseMessageBodyFollow = body;
      parseSubjectFollow = parseSubject;
    }

    // First skip leading dots and spaces
    boolean found = false;
    int pt1 = 0;
    while (pt1 < body.length() && " .".indexOf(body.charAt(pt1))>=0) pt1++;
    while (pt1 < body.length()) {
      while (pt1 < body.length() && body.charAt(pt1) == ' ') pt1++;
      if (pt1 >= body.length()) break;

      if (body.substring(pt1).startsWith("(Con't)")) break;

      char d1 = body.charAt(pt1);
      if (d1 != '(' && d1 != '[') break;

      char d2 = (d1 == '(' ? ')' : ']');
      int level = 0;
      int pt2;
      for (pt2 = pt1; pt2 < body.length(); pt2++) {
        char c = body.charAt(pt2);
        if (c == d1) level++;
        if (c == d2) level--;
        if (level == 0) {
          found = true;
          addSubject(body.substring(pt1+1, pt2).trim());
          pt1 = pt2+1;
          break;
        }
      }
      if (pt2 >= body.length()) break;
    }
    if (found || !options.splitKeepLeadBreak()) {
      body = trimLead(body.substring(pt1), true);
    }

    // If we didn't change anything, then reset any saved following msg information
    if (saveFollow && pt1 == 0) {
      parseMessageBodyFollow = parseSubjectFollow = null;
    }
    return body;
  }

  private void trimLast(StringBuilder sb, String endCode) {
    int len = sb.length()-endCode.length();
    if (len < 0) return;
    if (sb.substring(len).equals(endCode)) sb.setLength(len);
  }

  private void addSubject(String subject) {
    subject = subject.trim();
    if (subject.equals("FWD:") || subject.equals("FW:")) return;

    for (Pattern ptn : SUBJECT_HEADER_PTNS) {
      Matcher match = ptn.matcher(subject);
      if (match.find()) {
        msgIndex = match.group(1).charAt(0)-'0';
        msgCount = match.group(2).charAt(0)-'0';
        if (match.start() == 0) subject = subject.substring(match.end()).trim();
        else subject = subject.substring(0,match.start()).trim();
      }
    }
    if (parseSubject.length() == 0) parseSubject = subject;
    else parseSubject = parseSubject + '|' + subject;
  }

  private Matcher findPattern(String field, Pattern[] ptns) {
    for (Pattern ptn : ptns) {
      Matcher match = ptn.matcher(field);
      if (match.find()) return match;
    }
    return null;
  }

  /**
   * Set parsed message information
   * @param msgInfo
   */
  public void setInfo(MsgInfo msgInfo) {
    this.info = msgInfo;
  }

  /**
   * @return the parsed information object associated with this object
   * created by a previous call to MsgParser.isPageMsg(Message msg, int parserFlags)
   */
  public MsgInfo getInfo() {
    return info;
  }

  /**
   * Determine if message is expecting a suplemental message
   * @param options Split message processing options
   * @return true if it is
   */
  public boolean expectMore(SplitMsgOptions options) {

    // The rules change if the message parts may be misordered
    if (options.revMsgOrder() || options.mixedMsgOrder()) {

      // In which case, parse failure or general alert result may be a partial message
      if (info == null || info.getMsgType() == MsgType.GEN_ALERT) return true;
    }

    // If message matches expected break length, within the pad fudge factor, expect more to come
    int breakLen = options.splitBreakLength();
    if (breakLen > 0) {
      int delta = origMsgLen % breakLen;
      if (delta == 0) return true;
      if (delta + options.splitBreakPad() >= breakLen)  return true;
    }
    // Otherwise, message must parse and must return expect more status to be incomplete
    return info != null && info.isExpectMore();

  }

  /**
   * Escape a string containing tabs, newlines, or multiple spaces so it
   * will better survive transmission through an email message
   * @param message message to be escaped
   * @return escaped message
   */
  private static final Pattern MULT_SPACES = Pattern.compile(" {2,}");
  private static final Pattern UNPRINTABLE = Pattern.compile("[^\\p{Print}\n]");
  static public String escape(String message) {
    if (message == null) return message;
    message = message.replace("\\", "\\\\");
    message = message.replace("\t", "\\t");
    message = message.replace("\n", "\\n");
    message = message.replace("\r", "\\r");
    message = message.replace("\f", "\\f");
    message = message.replace("\b", "\\b");
    Matcher match = MULT_SPACES.matcher(message);
    if (match.find()) {
      StringBuffer sb = new StringBuffer();
      do {
        match.appendReplacement(sb, "\\\\" + (match.end()-match.start()) + "s");
      } while (match.find());
      match.appendTail(sb);
      message = sb.toString();
    }
    match = UNPRINTABLE.matcher(message);
    if (match.find()) {
      StringBuffer sb = new StringBuffer();
      do {
        int code = message.charAt(match.start());
        match.appendReplacement(sb, String.format("\\\\u%04x",code));
      } while (match.find());
      match.appendTail(sb);
      message = sb.toString();
    }
    return message;
  }

  /**
   * Determine if message needs to be reparsed after a split message option change
   * @param changeCode Level of split message option change<br>
   * 0 - No Change<br>
   * 1 - keep lead break option change<br>
   * 2 - insert blank option change<br>
   * 3 - merge message option change
   * @param body original message body text
   * @return true if message needs to be reformatted
   */
  public static boolean splitMsgOptionChange(int changeCode, String body) {
    if (changeCode == 0) return false;
    if (changeCode >= 3) return false;
    String[] lines = body.split("\n");
    if (lines.length == 1) return false;
    if (changeCode == 1) return true;
    for (String line : lines) {
      line =  line.trim();
      if (CONT_PTN.matcher(line).matches()) return true;
    }
    return false;
  }
}
