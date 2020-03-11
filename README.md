# GCRFs tool

Structured regression  models are designed to use relationships between objects for predicting output variables.  In other words, structured regression models consider the attributes of objects and dependencies between the objects to make predictions. 

The Gaussian Conditional Random Fields (GCRF) model is one type of structured regression models that incorporate the outputs of unstructured predictors (based on the given attributes values) and the correlation between output variables in order to achieve a higher prediction accuracy. 

The GCRFs tool is an open-source software that integrates vari-ous GCRF methods and supports training and testing of those methods on synthetic and real-world  datasets.   

<ul>
    <li> <a href="#i">Installation and User manual</a></li> 
    <li> <a href="#t">Technical requirements</a></li> 
  <li> <a href="#m">Methods</a></li> 
    <li> <a href="#d">Datasets</a></li> 
   <li> <a href="#a">Accuracy measure</a></li> 
   <li> <a href="#e"> Efficiency</a></li>
  <li> <a href="#r">References</a></li> 
   <li> <a href="#l"> External libraries</a></li> 
   <li> <a href="#c">Contibutors</a></li> 
     <li> <a href="#q">Questionnaire for tool evaluation</a></li> 
</ul>

<a id="i" class="anchor" aria-hidden="true" href="#c"></a>
# Installation and User manual

Download zip file <a href="https://github.com/vujicictijana/GCRF_GUI_TOOL/raw/master/gcrfs.zip">here</a>.<br>
Extract zip to the desired location. The extracted folder GCRFsTOOL contains the executable file gui.jar. <br> <br>
User manual is available <a href="https://github.com/vujicictijana/GCRF_GUI_TOOL/raw/master/Manual.pdf">here</a>.

<a id="t" class="anchor" aria-hidden="true" href="#t"></a>
# Technical requirements
<ul>
    <li>Java 8</li>
    <li>Matlab (optional, if you want to test following methods: UmGCRF, m-GCRF, up-GCRF, and RLSR)</li>
    </ul>

<a id="m" class="anchor" aria-hidden="true" href="#c"></a>
# Methods

Structured regression methods:

- Gaussian Conditional Random Fields (GCRF) is structured regression model that incorporates the outputs of unstructuredpredictors (based on the given attributes values) and the correlation between output vari-ables in order to achieve a higher prediction accuracy. 

- Directed  GCRF  (DirGCRF)  extends  the  GCRF  method  byconsidering asymmetric similarity (directed graphs).

- Unimodal GCRF (UmGCRF) extends the GCRF parameter spaceto allow negative influences and improves computational efficiency.

- Marginalized GCRF (m-GCRF) extends GCRF to naturallyhandle missing labels, rather than expecting the missing data to be treated in a preprocess-ing stage. 

- Uncertainty Propagation GCRF (up-GCRF) takes into ac-count uncertainty that comes from the data when estimating uncertainty of the predictions. 

- Representation  Learning  based  Structured  Regression  (RLSR) simultaneously learns hidden representation of objects and relationships among outputs.

Unstructured predictors:
- Neural networks
- Linear Regression
- Multivariate Linear Regression 

<a id="d" class="anchor" aria-hidden="true" href="#c"></a>
# Datasets

Tool provides 7 dataset samples.

<ul>
  <li> Geostep Asymmetric: 
    <ul>
       <li>Nodes: treasure hunt games - 25 for train, 25 for test <\li>
         <li>Network: similarity between games, based on common number of clues</li> 
 <li>Attributes: 6 - the number of clues in each category (business, social, travel, and irrelevant), game privacy scope, and game duration</li> 

 <li>Goal: predict probability that the game can be used for touristic purposes </li>

 <li>Note:Linear regression cannot be applied to this data. </li>
 </ul>
 </li>

  <li> Geostep Symmetric: 
<ul>
       <li>Similarity is converted from asymmetric to symmetric </li>

 <li>Note:
Linear regression cannot be applied to this data.</li></ul>
 </li>
 <li>Teen Asymmetric 1 x: 
  <ul>
<li>Nodes: 50 teenagers </li>

<li>Network: friendship network - teenagers were asked to identify up to 12 best friends </li>

<li>Attribute: teenager's alcohol consumption (ranging from 1 to 5) in previous time point </li>

<li>Goal: predict alcohol consumption at the observation time point </li>
</ul></li>

<li>Teen Asymmetric 3 x: <ul>
<li>Nodes: 50 teenagers </li>
<li>Network: friendship network - teenagers were asked to identify up to 12 best friends </li>
<li>Attributes: teenager's alcohol consumption (ranging from 1 to 5) in three previous time points </li>
<li>Goal: predict alcohol consumption at the observation time point </li>
</ul></li>
<li>Energy RLSR:  <ul>
<li>Nodes: 10 

<li>Network: no network (method should learn similarity)  </li>

<li>Attribute: 1 </li> 

<li>Goal: predict daily solar energy income  </li>

<li>Time points: 1600  </li>

</ul></li>
<li>Rain up-GCRF: <ul>
<li>Nodes: 100 </li>

<li>Network: no network (method should learn similarity) </li>

<li>Attribute: 2 </li>

<li>Goal: predict rainfall </li>

<li>Time points: 708 </li>

</ul></li>
<li>Random m-GCRF: 
<ul>
<li>Randomly generated data</li>

<li>Nodes: 20</li>

<li>Attributes: 3</li>

<li>Time points: 3</li>
</ul></li>
</ul>

Users can add their own dataset using <i>Add dataset</i> option in <i>Datasets</i> menu item.

<a id="a" class="anchor" aria-hidden="true" href="#c"></a>
# Accuracy measure

R<sup>2</sup>  coefficient of determination is used to calculate the regression accuracy of all methods. R<sup>2</sup>  measures how closely the output of the model matches the actual value of the data. A score of 0 indicates a very poor matching, while a score of 1 indicates a perfect match. 

<a id="r" class="anchor" aria-hidden="true" href="#c"></a>
# References
<ul>
<li>
GCRF: Vladan  Radosavljevic,  Slobodan  Vucetic,  and  Zoran  Obradovic.   Continuous  conditional random fields for regression in remote sensing. In Proccedings of European Conference on Artificial Intelligence (ECAI), pages 809–814, 2010 (<a href="http://www.ist.temple.edu/~zoran/papers/ECAI125.pdf">PDF</a>)
</li>
<li>
DirGCRF: Tijana Vujicic, Jesse Glass, Fang Zhou, and Zoran Obradovic. Gaussian conditional random fields extended for directed graphs. Machine Learning, 106(9-10):1271–1288, 2017 (<a href="http://www.dabi.temple.edu/~zoran/papers/tijanadmkd2017.pdf">PDF</a>)
</li>	
<li>
UmGCRF: Jesse  Glass,  Mohamed  F  Ghalwash,  Milan  Vukicevic,  and  Zoran  Obradovic.   Extending the  modelling  capacity  of  Gaussian  conditional  random  fields  while  learning  faster.   In Proceedings of the Thirtieth AAAI Conference on Artificial Intelligence (AAAI-16), pages1596–1602, 2016 (<a href="http://www.ist.temple.edu/~zoran/papers/jesseAAAI2016.pdf">PDF</a>)
</li>	
<li>
RLSR: Chao Han, Shanshan Zhang, Mohamed Ghalwash, Slobodan Vucetic, and Zoran Obradovic.Joint learning of representation and structure for sparse regression on graphs.  In Proceedings of the SIAM International Conference on Data Mining, pages 846–854, 2016 (<a href="http://www.ist.temple.edu/~zoran/papers/chaoSDM2016.pdf">PDF</a>)
</li>
<li>
up-GCRF: Djordje Gligorijevic, Jelena Stojanovic, and Zoran Obradovic.  Uncertainty propagation inlong-term  structured  regression  on  evolving  networks.   In Proceedings  of  the  Thirtieth  AAAI Conference on Artificial Intelligence (AAAI-16), pages 1603–1609, 2016 (<a href="http://www.ist.temple.edu/~zoran/papers/djoleAAAI2016.pdf">PDF</a>)
</li>
<li>
m-GCRF: Jelena  Stojanovic,  Milos  Jovanovic,  Djordje  Gligorijevic,  and  Zoran  Obradovic.   Semi-supervised  learning  for  structured  regression  on  partially  observed  attributed  graphs. In Proceedings  of  the  SIAM  International  Conference  on  Data  Mining,  pages 217–225, 2015 (<a href="http://www.dabi.temple.edu/~zoran/papers/StojanovicSDM15.pdf">PDF</a>)
</li>
</ul>

<a id="e" class="anchor" aria-hidden="true" href="#e"></a>
# Efficiency

The scalability of the tool and the running behaviour of different methods were assessed on different datasets with varying numbers of nodes: 100, 500, 1000 and 5000. All experiments were run on Windows with 16GB RAM memory and 3.4GHz CPU. The time consumption is presented after 50 iterations and the results are shown in the table below.

<table>
    <tr>
        <td>No. of nodes </td>
        <td>No. of edges </td>
        <td> GCRF </td>
        <td> DirGCRF</td>
        <td> UmGRCRF</td>
        <td> m-GRCRF</td>
        <td> up-GRCRF</td>
        <td> RLSR</td>
    </tr>
      <tr>
        <td>100 </td>
        <td>5,094 </td>
        <td> 0.27 s </td>
        <td>  0.17 s</td>
        <td>  6.62 s</td>
        <td>  8.49 s </td>
        <td> 26.84 s</td>
        <td>  69.25 s</td>
    </tr>
       <tr>
        <td>500  </td>
        <td>127,540  </td>
        <td> 16.98 s </td>
        <td>   9.49 s </td>
        <td>  7.45 s </td>
        <td>  17 s </td>
        <td> 6.58 min</td>
        <td>  8.25 min</td>
    </tr>
 <tr>
        <td>1000 </td>
        <td>509,376</td>
        <td>  129.4 s  </td>
        <td>   69.57 s </td>
        <td> 8 s </td>
        <td>  53.15 s </td>
        <td>27.6 min </td>
        <td>  1h 18 min </td>
    </tr>
 <tr>
     <td>5000</td>
        <td>12,749,518</td>
        <td>  4h 45 min </td>
        <td>   2h 12 min </td>
        <td> 34.48 s</td>
        <td>  65 min </td>
        <td>N/A</td>
        <td>  N/A</td>
    </tr>
</table>
    

<a id="l" class="anchor" aria-hidden="true" href="#l"></a>
# External libraries

<ul>
    <li> <a href="http://ojalgo.org/">OjAlgo</a> (oj! Algorithms) for matrix calculations</li>
    <li> <a href="http://neuroph.sourceforge.net/">Neuroph</a> for neural networks implementation</li>
    <li> <a href="https://code.google.com/archive/p/matlabcontrol/">Matlabcontrol</a> for calling Matlab from Java</li>
    </ul>
 
<a id="c" class="anchor" aria-hidden="true" href="#c"></a>
# Contibutors

- Tijana (Vujicic) Markovic
- Vladan Devedzic
- Fang Zhou
- Zoran Obradovic
- Jesse Glass
- Jelena Stojanovic
- Djordje Gligorijevic
- Chao Han
- Ivan Knezevic
- Petar Radunovic

<a id="q" class="anchor" aria-hidden="true" href="#q"></a>
# Questionnaire for tool evaluation

The main goal of GCRFs tool is to provide straightforward and user-friendly graphical user interface that will simplify the use of GCRF methods for expert and non-expert users. In order to get a detailed insight into the users’ experiences and opinions, we have created a questionnaire for tool evaluation.

Please fill out the <a href="https://goo.gl/forms/1kVVMLc1lVe1eYU32"> questionnaire</a>, we are opened for your opinions and suggestions.

