    /**
     * Created by Jaideep on 9/12/2016.
     */

    import org.jsoup.Jsoup;
    import org.jsoup.nodes.Document;
    import org.jsoup.nodes.Element;
    import org.jsoup.select.Elements;

    import java.io.IOException;
    import java.util.*;

    public class WebCrawl {
        String initialurl = "http://purdue.edu";
        Queue<String> travUrl = new LinkedList<String>();
        Hashtable<String, Integer> visited = new Hashtable<String, Integer>();
        Hashtable<String, Integer> depth = new Hashtable<String, Integer>();
        Queue<String> notvisited = new LinkedList<String>();
        int maxdepth = 0;
        int newdepth = 0;
        int numvisited = 0;
        Stack<String> stack = new Stack<String>();
        Hashtable<String, Integer> freq = new Hashtable<String, Integer>();
        int counter = 0;

        public void text_proc()throws IOException {
            notvisited.add("http://www.cs.purdue.edu");
            int n = 0;
            int sum1 = 0;
            while (n < 250) {
                System.out.println(n);
                try {
                    String oldUrl = notvisited.poll();
                    Document doc = null;
                    travUrl.add(oldUrl);
                   // if (!travUrl.contains(oldUrl)) {
                        doc = Jsoup.connect(oldUrl).timeout(0).get();
                        visited.put(oldUrl, 1); //parse text here
                      // travUrl.add(oldUrl);

                        System.out.println("Currently link is" + oldUrl);
                        //System.out.println(doc.body().text());
                        String texta = doc.body().text();
                        String newtext = texta.toLowerCase();
                        String a[] = newtext.split("\\P{Alpha}+");

                        for(String x : a)
                        {
                         //   x.toLowerCase();

                           //String temp[] =  x.split("\\P{Alpha}+");
                            //for(String y : temp) {
                                //System.out.println(x);
                                if (freq.containsKey(x) == false) {
                                    freq.put(x, 1);
                                    sum1++;
                                } else {
                                    freq.put(x, freq.get(x) + 1);
                                }
                            //}

                        }
                        //System.out.println(freq.toString());



                        Elements links = doc.select("a[href]");
                        for (Element link : links) {
                            String linkh = link.attr("abs:href");
                            if (travUrl.contains(linkh) == false) {
                                notvisited.add(linkh);
                                travUrl.add(linkh);
                            }


                        }
                   //}
                }
                catch (Exception e)
                {
                    continue;
                }
                n++;
            }
           // System.out.println(freq.size());


    /*Sorting */
            ArrayList<Map.Entry<String,Integer>> sorted = new ArrayList<>(freq.entrySet());

            Collections.sort(sorted, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });
    /*Top 10 sorted*/
    //before stopwords

            for(int i = 0; i<100;i++) //print 100
            {
              //  System.out.println(i+","+sorted.get(i));
            }

            for(int i = 0; i<10;i++) //print 10 most frequent
            {
                System.out.println(i+","+sorted.get(i));
            }



            int l = 0;
          /* for(l=0;l<100;l++)
           {
               System.out.println(sorted.get(l));
           }
    */
            Document doc = Jsoup.connect("http://ir.dcs.gla.ac.uk/resources/linguistic_utils/stop_words").timeout(0).get();
            String sword = doc.body().text();
            String a[] = sword.split("\\P{Alpha}+");
            int sum = 0;
            Hashtable<String,Integer> temp = new Hashtable<String, Integer>();

            System.out.println("average is: " + (freq.size()/250));
            temp.putAll(freq);

            //System.out.println("before"+freq.toString());

    //remove stop words
            for(String x : a)
            {

                //System.out.println(x);
                if(freq.containsKey(x)) {
                    //freqns.put(x, freq.get(x));
                    freq.remove(x);

                }
            }
            ArrayList<Map.Entry<String,Integer>> sorted1 = new ArrayList<>(freq.entrySet());

            Collections.sort(sorted1, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });


System.out.println("Sorted1");
    for (int i =0; i<10;i++)
    {
        System.out.println(i+","+sorted1.get(i));
    }

    for (int i = 0;i<100;i++)
    {
        System.out.println(i+","+sorted1.get(i));
    }

            System.out.println(freq.toString());
            Set<String> keys = freq.keySet();
            for(String key : keys)
            {
                sum+=freq.get(key);
            }

            System.out.println("Do not get "+sum);
            System.out.println(sum1);
            System.out.println("average is: " + (sum1/250));




            Hashtable<String,Integer> stemmed = new Hashtable<String,Integer>();
            Stemmer stemmer = new Stemmer();

            Set<String> keys2 = temp.keySet();
            for(String key: keys2) {
                for(int i=0;i<key.length();i++)
                {
                    stemmer.add(key.charAt(i));

                }
                stemmer.stem();
                String x = stemmer.toString();
                //System.out.println(x);
                if(stemmed.containsKey(x))
                {
                    stemmed.put(x,stemmed.get(x)+1);
                }
                else
                {
                    stemmed.put(x,1);
                }
            }

           // System.out.println(stemmed.toString());

            ArrayList<Map.Entry<String,Integer>> sorted2 = new ArrayList<>(stemmed.entrySet());

            Collections.sort(sorted2, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });

            for(int i = 0;i<10;i++)
            {
                System.out.println(i+","+sorted2.get(i));
            }

            for(int i = 0;i<100;i++)
            {
                //3System.out.println(i+","+sorted2.get(i));
            }



        }


        public void crawl_dfs()throws IOException {

            int maxdepth = 0;
            int currdepth = 0;
            depth.put(initialurl, 0);
            stack.push(initialurl);
            int n = 0;
            while (n < 1000) {
                try {
                    System.out.println(n);
                    System.out.printf("Current link is %s \n", stack.peek());
                    String oldUrl = stack.pop();
                    Document doc = null;
                    if (!visited.contains(oldUrl)) {
                        doc = Jsoup.connect(oldUrl).timeout(0).get();
                        visited.put(oldUrl, 1);
                        Elements links = doc.select("a[href]");
                        for (Element link : links) {
                            String linkh = link.attr("abs:href");
                            if (visited.containsKey(linkh) == false) { // if not
                                // visited then
                                // added to
                                // queue
                                currdepth = depth.get(oldUrl) + 1;
                                // System.out.println("CurrDerpth" + currdepth);
                               // if (stack.contains(linkh) == false)
                                    depth.put(linkh, depth.get(oldUrl) + 1);
                                if (maxdepth <= currdepth) {
                                    maxdepth = currdepth;
                                }

                                stack.push(linkh);
                                System.out.println(linkh);
                            }
                            else
                            {
                                counter++;
                                visited.put(linkh,visited.get(linkh)+1);
                            }
                        }
                    } else {
                        visited.put(oldUrl, visited.get(oldUrl) + 1);
                        counter++;
                    }
                }
                     catch (Exception e) {
                        //n++;
                        continue;
                    }
                    n++;

                }
                System.out.println(visited.toString());
                System.out.println(stack.size());
                System.out.println("Maxdepth is : " + maxdepth);
                System.out.println("Counter is : " + counter);

            //System.out.println(depth.toString());
            }





        public void crawl()throws IOException{
            int currdepth = 0;
            depth.put(initialurl, 0);
            notvisited.add(initialurl);
            int n = 0;
            while (n < 1000) {
                try {
                    System.out.println(n);
                    System.out.printf("Current link is %s \n", notvisited.element());
                    String oldUrl = notvisited.poll();
                    travUrl.add(oldUrl);
                    Document doc = null;

                   // if (!visited.contains(oldUrl)) {
                        doc = Jsoup.connect(oldUrl).timeout(0).get();
                        visited.put(oldUrl, 0);
                        //depth.put(oldUrl,0);

                        Elements links = doc.select("a[href]");

                        for (Element link : links) {
                            String linkh = link.attr("abs:href");

                            if (travUrl.contains(linkh) == true) { // if not
                                // traversed then
                                // added to
                                // queue

                                counter++;
                            } else {
                                //System.out.println("already added this to list");
                                //visited.put(linkh, visited.get(linkh) + 1);
                                //System.out.println("Insetred and updated");
                                currdepth = depth.get(oldUrl) + 1;
                                travUrl.add(linkh);

                                // System.out.println("CurrDerpth" + currdepth);
                                if (notvisited.contains(linkh) == false)
                                    depth.put(linkh, depth.get(oldUrl) + 1);
                                if (maxdepth <= currdepth) {
                                    maxdepth = currdepth;
                                }


                                // visited.put(linkh, 1);
                                //if (!linkh.contains(".pdf")
                                //      && !linkh.contains(".ico")) { //add these regardless
                                notvisited.add(linkh);

                                System.out.println(linkh);

                            }

                        }
                   // } else {
                    //    visited.put(oldUrl, visited.get(oldUrl) + 1);
                      //  counter++;

                   // }
                } catch (Exception e) {
                        //n++;//added
                    continue;
                }
                n++;

            }
            //System.out.println(visited.toString());
            System.out.println(notvisited.size());
            System.out.println("Maxdepth is : " + maxdepth);


            System.out.println("Counter is : " + (counter));

            //System.out.println(depth.toString());
        }

        public static void main(String[] args) throws IOException {
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter 1 for bfs, 2 for dfs, 3 for text processing");
            int a = scan.nextInt();


            WebCrawl craw = new WebCrawl();
            if(a==3) {
                craw.text_proc();
            }
            else if(a==1)
            {
                craw.crawl();
            }
            else
            {
           craw.crawl_dfs();
        }
           //String str = "Hell'o";
           //System.out.println(str.matches("[H][A-Za-z]+['][A-Za-z]+"));
        }
    }