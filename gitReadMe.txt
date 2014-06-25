The fact you are reading this added to the fact the code is in github should 
mean you have git installed.

Given that (else install git for your ide/Operating system )....

The basic branching is:
master 	- the main trunk from which release builds are done
develop - the main develop branch which can either be used for development if
		engaged in a total visibility dev style or can be further branched from.
		
We are trying to use jgitflow ( https://bitbucket.org/atlassian/jgit-flow ) to
automate the branching as per:

http://nvie.com/posts/a-successful-git-branching-model/

Check the documentation e.g.

https://bitbucket.org/atlassian/jgit-flow/wiki/goals.wiki

If not...

Nota Bene: No merging into Master. We run a gatekeeper style of operation with 
checks etc. Prior to going into master.

A simple merging example between develop & develop-bob.

You have done some work in develop-bob. Now you want to bring it into develop.

Commit and push all your work.
then:
checkout develop
pull origin/develop-bob


