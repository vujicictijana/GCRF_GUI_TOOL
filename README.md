# GCRFs tool

Structured regression  models are designed to use relationships between objects for predicting output variables.  In other words, structured regression models consider the attributes of objects and dependencies between the objects to make predictions. 

The Gaussian Conditional Random Fields (GCRF) model is one type of structured regression models that incorporate the outputs of unstructured predictors (based on the given attributes values) and the correlation between output variables in order to achieve a higher prediction accuracy. 

The GCRFs tool is an open-source software that integrates vari-ous GCRF methods and supports training and testing of those methods on synthetic and real-world  datasets.   

<ul>
  <li> <a href="#m">Methods included</a></li> 
  
   <li> <a href="#c">Contibutors</a></li> 
</ul>


<a id="m" class="anchor" aria-hidden="true" href="#c"></a>
# Methods included

- Gaussian Conditional Random Fields (GCRF) is structured regression model that incorporates the outputs of unstructuredpredictors (based on the given attributes values) and the correlation between output vari-ables in order to achieve a higher prediction accuracy. 

- Directed  GCRF  (DirGCRF)  extends  the  GCRF  method  byconsidering asymmetric similarity (directed graphs).

- Unimodal GCRF (UmGCRF) extends the GCRF parameter spaceto allow negative influences and improves computational efficiency.

- Marginalized GCRF (m-GCRF) extends GCRF to naturallyhandle missing labels, rather than expecting the missing data to be treated in a preprocess-ing stage. 

- Uncertainty Propagation GCRF (up-GCRF) takes into ac-count uncertainty that comes from the data when estimating uncertainty of the predictions. 

- Representation  Learning  based  Structured  Regression  (RLSR) simultaneously learns hidden representation of objects and relationships among outputs.

# Accuary measure

R<sup>2</sup>  coefficient of determination is used to calculate the regression accuracy of all methods. R<sup>2</sup>  measures how closely the output of the model matches the actual value of the data. A score of 0 indicates a very poor matching, while a score of 1 indicates a perfect match. 


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

