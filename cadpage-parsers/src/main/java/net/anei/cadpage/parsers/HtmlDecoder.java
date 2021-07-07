package net.anei.cadpage.parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Our job is to break standard HTML text into an array of data fields that
 * can be processed with FieldProgramParser.  Class is not thread safe
 */

public class HtmlDecoder {

  // Different HTML flags that tell the decoder what should be done with
  // the different html tags

  // All data should be ignored until we find the matching close tag
  private static final int HTML_FLAG_SKIP_DATA = 0x0001;

  // Tag should trigger a field break
  private static final int HTML_FLAG_FIELD_BREAK = 0x0002;

  // Tag should generate a line break
  private static final int HTML_FLAG_LINE_BREAK = 0x0004;

  // User identified break tag
  private static final int HTML_FLAG_USER_BREAK = 0x0008;

  private static final int HTHL_FLAG_PRE =0x0010;


  private static final char EOL = (char)-1;

  private boolean preserveWhitespace = false;

  // Primary map that tells us how to process HTML tags
  private Map<String, Integer> tagDictionary = new HashMap<String,Integer>();

  public HtmlDecoder() {
    this(null, false);
  }

  public HtmlDecoder(boolean preserveWhitespace) {
    this(null, preserveWhitespace);
  }

  public HtmlDecoder(String userTags) {
    this(userTags, false);
  }

  public HtmlDecoder(String userTags, boolean preserveWhitespace) {
    setTagFlags(HTML_FLAG_SKIP_DATA, "head", "style");
    setTagFlags(HTML_FLAG_LINE_BREAK, "br");
    setTagFlags(HTML_FLAG_FIELD_BREAK,
        "blockqute", "code", "dd", "div", "dl", "dt",
        "h1", "h2", "h3", "h4", "H5", "h6",
        "li", "p", "pre", "section",
        "table", "tbody", "td", "th", "thead", "tr", "ul");
    setTagFlags(HTHL_FLAG_PRE, "pre");
    if (userTags != null) {
      setTagFlags(HTML_FLAG_USER_BREAK | HTML_FLAG_FIELD_BREAK, userTags.split("\\|"));
    }
    this.preserveWhitespace = preserveWhitespace;
  }


  private void setTagFlags(int flags, String ... tags) {
    for (String tag : tags) {
      tagDictionary.put(tag, flags);
    }
  }

  public void setPreserveWhitespace(boolean preserveWhitespace) {
    this.preserveWhitespace = preserveWhitespace;
  }

  // Working variables
  private String body;
  private int pos;
  private List<String> fieldList;
  private StringBuilder curField;
  private boolean space;
  private boolean lineBreak;
  private Set<String> userTagSet;
  private int nestedPreCnt;

  /**
   * Main parsing method
   * @param body message text to be parsed
   * @return array of data fields if successful, null if parse fails
   */
  public String[] parseHtml(String body) {
    initialize(body);

    // Go into loop getting tags and figuring out what to do with them
    while (true) {

      // Get next html tag.  If EOS break out
      String tag = getNextTag(false);
      if (tag == null) break;

      // Get the flags associated with this tag
      // For this purpose we do not care whether it is a start tag,
      // end tag, or self completing tag.  If not flags are found
      // ignore it and skip to next tag
      String tag2 = tag.replace("/", "");
      Integer iFlags = tagDictionary.get(tag2);
      if (iFlags == null) continue;
      int tagFlags = iFlags;

      // Skip data flag tells us to skip over everything until we find
      // a matching close tag
      if ((tagFlags & HTML_FLAG_SKIP_DATA) != 0) {
        if (!tag.contains("/")) {
          while (true) {
            String ttag = getNextTag(true);
            if (ttag == null) break;
            if (ttag.contains("/") && ttag.replace("/", "").equals(tag)) break;
          }
        }
      }

      // Field break flag tells us to move the current data into a new field
      if ((tagFlags & HTML_FLAG_FIELD_BREAK) != 0) {
        addCurField();
        if ((tagFlags & HTML_FLAG_USER_BREAK) != 0) {
          if (userTagSet == null) userTagSet = new LinkedHashSet<String>();
          userTagSet.add(tag);
        }
      }

      // Line break flags tells us to request a line break be inserted in curField
      if ((tagFlags & HTML_FLAG_LINE_BREAK) != 0) {
        if (curField.length() > 0) lineBreak = true;
      }

      // <pre> tag increments or decrements the nested pre counter
      if ((tagFlags & HTHL_FLAG_PRE) != 0) {
        if (tag.startsWith("/")) nestedPreCnt--;
        else nestedPreCnt++;
      }
    }

    // ALl done, add any leftover data to the field list
    addCurField();

    // Success, calculate the return result, release all resources, and
    // return the result;
    String[] result = fieldList.toArray(new String[fieldList.size()]);
    releaseAll();
    return result;
  }

  /**
   * Initialize working variables
   * @param body message body
   */
  private void initialize(String body) {
    this.body = body;
    pos = 0;
    fieldList = new ArrayList<String>();
    curField = new StringBuilder();
    space = false;
    lineBreak = false;
    userTagSet = null;
    nestedPreCnt = 0;
  }

  /**
   * Release intermediate parsing resources
   */
  private void releaseAll() {
    body = null;
    fieldList = null;
    curField = null;
  }

  /**
   * Retrieve next HTML tag from message body.  Any actual data values
   * passed along the way will be appended to curField;
   * @param skipData true if data found between tags should be ignored, false
   * if it should be accumulated
   * @return HTML tag name converted to lower case if found, null if end of string reached
   */
  private String getNextTag(boolean skipData) {

    while (true) {
      // Get next character.  If no more characters, return null
      char chr  = getNextChar();
      if (chr == EOL) return null;

      // If start of html tag, retrieve html tag name
      if (chr == '<') {
        String tag = getHtmlTag();
        if (tag != null) return tag;
      }

      // If this is an HTML escape sequence, retrieve the escaped character
      if (chr == '&') chr = getHtmlEscape();

      // If whitespace character processing depends on whether or not
      // we are in a nested <pre> block
      if (Character.isWhitespace(chr)) {

        // Normal processing just sets teh space flag
        if (!preserveWhitespace && nestedPreCnt == 0) {
          if (curField.length() > 0) space = true;
        }

        // Pre block processing
        // new lines are treated as field breaks
        // Anything else is treated as a single blank
        else {
          if (chr == '\n') {
            addCurField(true);
          } else {
            curField.append(' ');
          }
        }
      }

      // Otherwise, append to current field, possibly with a leading space
      else if (!skipData) {
        if (userTagSet != null) {
          for (String tag : userTagSet) {
            fieldList.add("<|" + tag + "|>");
          }
          userTagSet = null;
        }
        if (lineBreak) curField.append('\n');
        else if (space) curField.append(' ');
        curField.append((char)chr);
        lineBreak = space = false;
      }
    }
  }

  /**
   * Retrieve the rest of an html tag after the initial < has been identified
   * @return html tag name if identified, null if no valid html tag is identified
   */
  private String getHtmlTag() {
    int savePos = pos;

    if (checkNext("!--")) {
      int tmp = body.indexOf("-->", pos);
      pos = tmp < 0 ? body.length() : tmp+3;
      return "!----";
    }

    StringBuilder sb = new StringBuilder();
    char chr = getNextChar();
    if (chr == '?') {
      sb.append(chr);
      chr = getNextChar();
    }
    if (chr == '/') {
      sb.append(chr);
      chr = getNextChar();
    }
    if (!Character.isLetter(chr)) {
      sb.append(Character.toLowerCase(chr));
      chr = getNextChar();
    }
    while (Character.isLetter(chr) || Character.isDigit(chr) || chr == ':') {
      sb.append(Character.toLowerCase(chr));
      chr = getNextChar();
    }
    if (sb.charAt(0)!='/' && chr == '/') {
      sb.append(chr);
      chr = getNextChar();
    }
    if (chr != ' ' && chr != '>' && chr != EOL) {
      pos = savePos;
      return null;
    }
    if (chr != EOL) {
      while (chr != '>' && chr != EOL) {
        chr = getNextChar();
        if (chr == '<') {
          pos = savePos;
          return null;
        }
      }
    }
    return sb.toString();
  }

  /**
   * Retrieve the rest of an HTML escape sequence after an & has been identified
   * @return the unescaped character
   */
  private char getHtmlEscape() {
    char code;
    int savePos = pos;
    String token = getUntil(';');
    if (token.startsWith("#")) {
      token = token.substring(1);
      try {
        if (token.startsWith("x") || token.startsWith("X")) {
          code = (char)Integer.parseInt(token.substring(1), 16);
        } else {
          code = (char)Integer.parseInt(token);
        }
      } catch (NumberFormatException ex) {
        code = '&';
        pos = savePos;
      }
    }
    else {
      Character res = ESCAPE_CODES.get(token);
      if (res != null) {
        code = res;
      } else {
        code = '&';
        pos = savePos;
      }
    }

    return code;
  }

  /**
   * Retrieve all text until terminator character found
   * @param mark terminator character
   * @return String containing all text up to terminator character
   */
  private String getUntil(int mark) {
    StringBuilder sb = new StringBuilder();
    while (true) {
      char chr = getNextChar();
      if (chr == EOL || chr == mark) return sb.toString();
      sb.append(chr);
    }
  }

  /**
   * If current data field has any data, move it to the field list and clear
   * the current data field
   */
  private void addCurField() {
    addCurField(false);
  }

  /**
   * If current data field has any data, move it to the field list and clear
   * the current data field
   * @param lineBreak - true of field end triggered by newline character
   */
  private void addCurField(boolean lineBreak) {
    if (!lineBreak && curField.length() == 0) return;
    fieldList.add(curField.toString());
    curField.setLength(0);
    lineBreak = false;
    space = false;
  }

  private boolean checkNext(String field) {
    if (!body.substring(pos).startsWith(field)) return false;
    pos += field.length();
    return true;
  }

  /**
   * Retrieve next character from message body
   * @return next character or -1 if end of string reached
   */
  private char getNextChar() {
    if (pos >= body.length()) return EOL;
    return body.charAt(pos++);
  }

  private static Map<String, Character> ESCAPE_CODES = new HashMap<String, Character>();
  static {
    ESCAPE_CODES.put("nbsp", ' ');
    ESCAPE_CODES.put("quot", '"');
    ESCAPE_CODES.put("amp", '&');
    ESCAPE_CODES.put("lt", '<');
    ESCAPE_CODES.put("gt", '>');
    ESCAPE_CODES.put("iexcl", '¡');
    ESCAPE_CODES.put("cent", '¢');
    ESCAPE_CODES.put("pound", '£');
    ESCAPE_CODES.put("curren", '¤');
    ESCAPE_CODES.put("yen", '¥');
    ESCAPE_CODES.put("brvbar", '¦');
    ESCAPE_CODES.put("sect", '§');
    ESCAPE_CODES.put("uml", '¨');
    ESCAPE_CODES.put("copy", '©');
    ESCAPE_CODES.put("ordf", 'ª');
    ESCAPE_CODES.put("not", '¬');
    ESCAPE_CODES.put("shy", ' ');
    ESCAPE_CODES.put("reg", '®');
    ESCAPE_CODES.put("macr", '¯');
    ESCAPE_CODES.put("deg", '°');
    ESCAPE_CODES.put("plusmn", '±');
    ESCAPE_CODES.put("sup2", '²');
    ESCAPE_CODES.put("sup3", '³');
    ESCAPE_CODES.put("acute", '´');
    ESCAPE_CODES.put("micro", 'µ');
    ESCAPE_CODES.put("para", '¶');
    ESCAPE_CODES.put("middot", '·');
    ESCAPE_CODES.put("cedil", '¸');
    ESCAPE_CODES.put("sup1", '¹');
    ESCAPE_CODES.put("ordm", 'º');
    ESCAPE_CODES.put("raquo", '»');
    ESCAPE_CODES.put("frac14", '¼');
    ESCAPE_CODES.put("frac12", '½');
    ESCAPE_CODES.put("frac34", '¾');
    ESCAPE_CODES.put("iquest", '¿');
    ESCAPE_CODES.put("Agrave", 'À');
    ESCAPE_CODES.put("Aacute", 'Á');
    ESCAPE_CODES.put("Acirc", 'Â');
    ESCAPE_CODES.put("Atilde", 'Ã');
    ESCAPE_CODES.put("Auml", 'Ä');
    ESCAPE_CODES.put("Aring", 'Å');
    ESCAPE_CODES.put("AElig", 'Æ');
    ESCAPE_CODES.put("Ccedil", 'Ç');
    ESCAPE_CODES.put("Egrave", 'È');
    ESCAPE_CODES.put("Eacute", 'É');
    ESCAPE_CODES.put("Ecirc", 'Ê');
    ESCAPE_CODES.put("Euml", 'Ë');
    ESCAPE_CODES.put("Igrave", 'Ì');
    ESCAPE_CODES.put("Iacute", 'Í');
    ESCAPE_CODES.put("Icirc", 'Î');
    ESCAPE_CODES.put("Iuml", 'Ï');
    ESCAPE_CODES.put("ETH", 'Ð');
    ESCAPE_CODES.put("Ntilde", 'Ñ');
    ESCAPE_CODES.put("Ograve", 'Ò');
    ESCAPE_CODES.put("Oacute", 'Ó');
    ESCAPE_CODES.put("Ocirc", 'Ô');
    ESCAPE_CODES.put("Otilde", 'Õ');
    ESCAPE_CODES.put("Ouml", 'Ö');
    ESCAPE_CODES.put("times", '×');
    ESCAPE_CODES.put("Oslash", 'Ø');
    ESCAPE_CODES.put("Ugrave", 'Ù');
    ESCAPE_CODES.put("Uacute", 'Ú');
    ESCAPE_CODES.put("Ucirc", 'Û');
    ESCAPE_CODES.put("Uuml", 'Ü');
    ESCAPE_CODES.put("Yacute", 'Ý');
    ESCAPE_CODES.put("THORN", 'Þ');
    ESCAPE_CODES.put("szlig", 'ß');
    ESCAPE_CODES.put("agrave", 'à');
    ESCAPE_CODES.put("aacute", 'á');
    ESCAPE_CODES.put("acirc", 'â');
    ESCAPE_CODES.put("atilde", 'ã');
    ESCAPE_CODES.put("auml", 'ä');
    ESCAPE_CODES.put("aring", 'å');
    ESCAPE_CODES.put("aelig", 'æ');
    ESCAPE_CODES.put("ccedil", 'ç');
    ESCAPE_CODES.put("egrave", 'è');
    ESCAPE_CODES.put("eacute", 'é');
    ESCAPE_CODES.put("ecirc", 'ê');
    ESCAPE_CODES.put("euml", 'ë');
    ESCAPE_CODES.put("igrave", 'ì');
    ESCAPE_CODES.put("iacute", 'í');
    ESCAPE_CODES.put("icirc", 'î');
    ESCAPE_CODES.put("iuml", 'ï');
    ESCAPE_CODES.put("eth", 'ð');
    ESCAPE_CODES.put("ntilde", 'ñ');
    ESCAPE_CODES.put("ograve", 'ò');
    ESCAPE_CODES.put("oacute", 'ó');
    ESCAPE_CODES.put("ocirc", 'ô');
    ESCAPE_CODES.put("otilde", 'õ');
    ESCAPE_CODES.put("ouml", 'ö');
    ESCAPE_CODES.put("divide", '÷');
    ESCAPE_CODES.put("oslash", 'ø');
    ESCAPE_CODES.put("ugrave", 'ù');
    ESCAPE_CODES.put("uacute", 'ú');
    ESCAPE_CODES.put("ucirc", 'û');
    ESCAPE_CODES.put("uuml", 'ü');
    ESCAPE_CODES.put("yacute", 'ý');
    ESCAPE_CODES.put("thorn", 'þ');
  }
}
