package com.example.codekamon;

public class QRCode {

    private double latitude;
    private double longitude;

    private int score;
    private String content;

    public QRCode(String content)
    {
        this.content = content;
        this.score = calcScore();
    }

    public String getContent()
    {
        return content;
    }

    public int getScore()
    {
        return score;
    }

    public int calcScore()
    {
        //needs further testing
        int returner = 0;
        char recChar;
        int count = 0;
        for(int i = 0; i < content.length(); i++)
        {

            if(i >= 1 && (content.charAt(i) == content.charAt(i - 1)))
            {
                count ++;
            }else if(i >= 1)
            {
                if(content.charAt(i - 1) == '0')
                {
                    returner += Math.pow(20,count);
                }
                else if('1' <= content.charAt(i - 1) && content.charAt(i - 1) <= '9' && count > 0)
                {
                    returner += Math.pow((int)content.charAt(i - 1), count);

                }
                else if('a' <= content.charAt(i - 1) && content.charAt(i - 1) <= 'f' && count > 0)
                {
                    returner += Math.pow(content.charAt(i - 1) + 10 - 'a', count);
                }
                count = 0;
            }
            recChar = content.charAt(i);
        }
        if(content.charAt(content.length()-1) == '0')
        {
            returner += Math.pow(20,count);
        }
        else if('1' <= content.charAt(content.length()-1) && content.charAt(content.length()-1) <= '9' && count > 0)
        {
            returner += Math.pow((int)content.charAt(content.length()-1), count);

        }
        else if('a' <= content.charAt(content.length()-1) && content.charAt(content.length()-1) <= 'f' && count > 0)
        {
            returner += Math.pow(content.charAt(content.length()-1) + 10 - 'a', count);
        }

        return returner;
    }
}


