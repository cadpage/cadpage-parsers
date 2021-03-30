package net.anei.cadpage.parsers;

public class MessageBuilder {

  private MsgParser parser;
  private SplitMsgOptions options;

  private boolean preserveProgram = false;

  private Message [] msgList;

  public MessageBuilder(MsgParser parser, SplitMsgOptions options) {
    this.parser = parser;
    this.options = options;
  }

  /**
   * In normal operation, the parser will be invoked multiple times
   * and the field program settings from the most recent attempt will
   * most likely not be the one that was used to retrieve the best result.
   * This is called if it is important to the parser return the field
   * term list for the best result
   */
  public void setPreserveProgram() {
    preserveProgram = true;
  }

  /**
   * Accumulate one or more message parts into a single Message object
   * @param msgList array of message parts
   * @param lock true of message order is locked
   * @return
   */
  public Message buildMessage(Message[] msgList, boolean lock) {

    // Life gets easy if there is only one message
    if (msgList.length == 1) {
      Message msg = msgList[0];
      return bldMessage(msg.getFromAddress(), msg.getSubject(false), msg.getMessageBody(true, false),
                        msg.getOrigMsgLen(), false);
    }

    // Reverse the message order is so requested
    if (options.revMsgOrder()) {
      Message[] tmp = new Message[msgList.length];
      for (int j = 0; j<msgList.length; j++) {
        tmp[j] = msgList[msgList.length-j-1];
      }
      msgList = tmp;
    }

    // Only slightly less easier if the message order is known
    if (lock || !options.mixedMsgOrder()) {
      return bldMessage(msgList);
    }

    // Otherwise life gets complicated.
    this.msgList = msgList;

    int[] msgOrder = new int[msgList.length];

    // Now thing get complicated.  We are going to step through each
    // position by pairs.  Stopping just before the last one because
    // the last position can always be autofilled
    for (int n = 1; n < msgList.length; n+=2) {

      // for each pair, try all combinations of two
      // parts from the remaining parts
      resetBestResult();
      for (int p1 = 0; p1 < msgList.length; p1++) {
        if (isIndexUsed(p1, msgOrder, n-1)) continue;
        msgOrder[n-1] = p1;
        if (n+1 == msgList.length) {
          trialParse(msgOrder, n);
        }
        else {
          for (int p2 = 0; p2 < msgList.length; p2++) {
            if (isIndexUsed(p2, msgOrder, n)) continue;
            msgOrder[n] = p2;
            trialParse(msgOrder, n+1);
          }
        }
      }

      // Then copy the best result back to our working message order array
      int[] bestOrder = bestResult.getMsgOrder();
      System.arraycopy(bestOrder, 0, msgOrder, 0, bestOrder.length);
    }

    // We have a result!!!!!
    if (preserveProgram) parser.setFieldList(bestProgram);
    return bestResult.getMessage();
  }

  private int bestScore;
  private ParseResult bestResult;
  private String  bestProgram;

  private void resetBestResult() {
    bestScore = Integer.MIN_VALUE;
    bestResult = null;
    bestProgram = null;
  }

  /**
   * Perferm a trial parse for a particular message order
   * @param msgOrder Array containing the message order indexes
   * @param n Number of elements that have been set in msgOrder
   */
  private void trialParse(int[] msgOrder, int n) {
    boolean incomplete = n < msgOrder.length-1;
    ParseResult result = new ParseResult(bldWorkingMsgOrder(msgOrder, n), incomplete);
    int score = result.getScore();
    if (score > bestScore) {
      bestScore = score;
      bestResult = result;
      if (preserveProgram) bestProgram = parser.getProgram();
    }
  }

  /**
   * Construct a working msg order array from a temporary working array
   * If all but the last mesage order has been assigned, the last index
   * will be autofilled with the remaining index
   * @param msgOrder working message order array
   * @param n number of elements to use in message order array
   * @return a pristine array of message indexes to be used to construct message
   */
  private int[] bldWorkingMsgOrder(int[] msgOrder, int n) {
    int len = (n == msgOrder.length-1 ? msgOrder.length : n);
    int[] result = new int[len];
    System.arraycopy(msgOrder, 0, result, 0, n);
    fillLast(result);
    return result;
  }

  /**
   * If a message index array is exactly matches the number of message parts
   * auto fill the last element with the remaining index value
   * @param msgOrder message index array
   */
  private void fillLast(int[] msgOrder) {
    if (msgOrder.length != msgList.length) return;
    int n = msgOrder.length-1;
    for (int ii = 0; ii < msgOrder.length; ii++) {
      if (isIndexUsed(ii, msgOrder, n)) continue;
      msgOrder[n] = ii;
      return;
    }
    throw new RuntimeException("fillLast could not find final index");
  }

  /**
   * Determine if an index is already used in the n elements of an array
   * @param ndx index to be checked
   * @param msgOrder message order array
   * @param n number of elements to check
   * @return true if index is found
   */
  private boolean isIndexUsed(int ndx, int[] msgOrder, int n) {
    for (int jj = 0; jj < n; jj++) {
      if (ndx == msgOrder[jj]) return true;
    }
    return false;
  }

  /**
   * saves status of one attempt to parse a particular combination of message parts
   * The message part array is passed to the constructor and must not be modified
   * afterwards
   */
  private class ParseResult {
    private int[] msgOrder;
    private Message result;
    private int score;

    public ParseResult(int[] msgOrder, boolean incomplete) {
      this.msgOrder = msgOrder;
      result = bldMessage(msgOrder);
      MsgInfo info = result.getInfo();
      if (info != null) {
        score = info.score(incomplete);
      } else {
        score = Integer.MIN_VALUE+1;
      }

      // For the tie breaker, favor combinations in which the
      // last segment is significantly shorter than the others
      // Unless this is an incomplete trial, in which case this
      // preference is reversed
      int minLen = Integer.MAX_VALUE;
      for (int j = 0; j<msgOrder.length-1; j++) {
        minLen = Math.min(minLen, msgList[msgOrder[j]].getMessageBody(true, j>0).length());
      }
      boolean shortLast = msgList[msgOrder[msgOrder.length-1]].getMessageBody(true, true).length() < minLen-5;
      if (incomplete ^ shortLast) score++;
    }

    public int[] getMsgOrder() {
      return msgOrder;
    }

    public int getScore() {
      return score;
    }

    public Message getMessage() {
      return result;
    }
  }

  /**
   * Construct a Message object from a text message
   * @param fromAddress message sender
   * @param subject message subject
   * @param body the text message
   * @param body original text message length
   * @param multi true if message text is an accumulation of multiple alert messages
   * @return result Message object
   */
  private Message bldMessage(String fromAddress, String subject, String body, int origMsgLen, boolean multi) {
    Message result = new Message(false, fromAddress, subject, body, options);
    result.setOrigMsgLen(origMsgLen);
    int flags = MsgParser.PARSE_FLG_FORCE | MsgParser.PARSE_FLG_SKIP_FILTER;
    if (multi) flags |= MsgParser.PARSE_FLG_MULTI;
    parser.isPageMsg(result, flags);
    return result;
  }

  /**
   * Construct a text message from different message parts in specified order
   * @param msgOrder array containing the message indexes to be used
   * @return the complete text message
   */
  private Message bldMessage(int[] msgOrder) {
    Message[] mList = new Message[msgOrder.length];
    for (int jj = 0; jj < msgOrder.length; jj++) {
      mList[jj] = msgList[msgOrder[jj]];
    }
    return bldMessage(mList);
  }

  /**
   * Construct a message from an array of message parts
   * @param msgBodyList array of message parts
   * @return the complete text message
   */
  private Message bldMessage(Message[] msgList) {
    boolean insBreak = options.splitBreakIns();
    boolean insBlank = options.splitBlankIns();
    int breakLen = options.splitBreakLength();
    int breakPad = options.splitBreakPad();
    boolean insCondBrk = options.insConditionalBreak();
    int lastLen = -1;
    int origMsgLen = 0;

    String fromAddress = null;
    String subject = "";

    boolean follow = false;
    StringBuilder sb = new StringBuilder();
    for (Message msg : msgList) {

      if (fromAddress == null) fromAddress = msg.getFromAddress();

      String tmp = msg.getSubject(follow);
      if (tmp.length() > subject.length()) subject = tmp;
      String msgText = msg.getMessageBody(parser.keepLeadBreak(), follow);
      follow = true;

      if (sb.length() > 0) {
        if (breakLen > 0 && breakPad > 0) {
          int delta = breakLen-lastLen;
          if (delta > 0 && delta <= breakPad) {
            if (delta >= 2 && insCondBrk) {
              sb.append('\n');
            } else {
              for (int jj = 0; jj<delta; jj++) sb.append(' ');
            }
          }
        }
        else if (insBreak) sb.append('\n');
        else if (insBlank) sb.append(' ');
      }

      sb.append(msgText);
      lastLen = msg.getOrigMsgLen();

      origMsgLen += msg.getOrigMsgLen();
    }
    return bldMessage(fromAddress, subject, sb.toString(), origMsgLen, msgList.length > 1);
  }
}
