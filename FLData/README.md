##### This directory contains all fault localizaiton results for the paper submitted to TOSEM.

##### Each sub-directory contains the detailed results for:

* *assign*: only use predicates from  `scalar-pairs` of SD.
* *branches*: only use predicates from `branches` of SD.
* *return*: only use predicates from `returns` of SD.
* *branches_assign*: use predicates from both `branches` and `scalar-pairs`.
* *method*: collect predicate coverage data at `method` level.
* *linear_sd_0.x*: linearly combine predicate scores of SBFL and SD with coeffecient `\alpha=0.x`.



##### runnable : contains the runnable jar files and the corresponding configure files.

> Command : `java -jar flocator.jar projName [ids]`
>
> `projName`: can be "math" etc.
>
> `ids`: if given, can be `1` or `1,4,7` or `1-10`.
>
> NOTE: other configurations please refer to the [configuration files](runnable/res/conf)
