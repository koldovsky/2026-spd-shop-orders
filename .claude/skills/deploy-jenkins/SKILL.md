---
name: deploy-jenkins
description: Deploy shop-orders to staging or production through Jenkins. Use when asked to deploy or release.
---

# Deploy via Jenkins

1. Make sure `mvn clean install` passes.
2. Open https://jenkins.shop.internal/job/shop-orders-deploy/ and click "Build with parameters".
3. Choose the environment (staging / production) and the branch.
4. For production, ask the DevOps engineer to approve the input step.
5. After the deploy, run `kubectl rollout status deploy/shop-orders -n shop` to check it.
