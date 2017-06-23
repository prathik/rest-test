![alt text](logo.png)

microQ is a Github service that sees if a particular pull request has test cases associated with it.

# How it works?

When ever a pull request is raised, edited, a check is made to the service to see if there are any test cases associated
with the pull request and if it is not present then the pull request is marked as invalid. If tests are
present against a pull request then it is marked as good.

# How to create a pull request?

```shell
curl -X POST \
  http://<host>/tc/clarity-client/ \
  -H 'content-type: application/json' \
  -d '{
        "name": "randomTest",
        "httpMethod": "GET",
        "expectedResponse": null,
        "host": "bizops-rna-engg1008.bizops-rna.blr1.inmobi.com",
        "user": "prathik.raj",
        "service": "clarity-client",
        "pullRequest": 0,
        "body": "aggga"
    }'
```

In the above case `clarity-client` is the service name. Service name is the same as your repository name.
