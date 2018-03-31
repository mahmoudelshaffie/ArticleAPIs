# ArticleAPIs

Decisions Taken:
# Using string processing, instead of using physical relations
I designed Artilce entity, to maintain list of authors and keywords in a single string columns. To avoid hassles of managing those relations, specially keywords of article.

# I Simplified Exception Handling Logic
I used spring based annotations, for exception handling instead of usage of a global exception handler. Since its is a trivial handling logic.

# Usage of assertion and stubbing utils
To make test cases more readable, and maintainable. 


 